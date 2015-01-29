package callete.api.services.impl.network;

import callete.api.Callete;
import callete.api.services.http.HttpService;
import callete.api.services.impl.network.model.HostAPDRepresentation;
import callete.api.services.impl.network.model.HotSpotRepresentation;
import callete.api.services.impl.network.model.WLANRepresentation;
import callete.api.services.network.HotSpot;
import callete.api.services.template.TemplateService;
import callete.api.services.template.TemplateSet;
import callete.api.util.FileUtils;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * The hot spot implementation, implemented for the Raspi.
 */
public class HotSpotImpl implements HotSpot {
  private final static Logger LOG = LoggerFactory.getLogger(HotSpotImpl.class);

  private final static String SUBNET_MASK = "255.255.255.0";

  private final static String INTERFACES_CONF = "/etc/network/interfaces";
  private final static String HOST_APD_CONF = "/etc/hostapd/hostapd.conf";
  private final static String RUN_CMD = "sudo hostapd -dd " + HOST_APD_CONF;


  private final static String HOST_APD_TEMPLATE = "hostapd.conf";
  private final static String INTERFACES_HOTSPOT_TEMPLATE = "interfaces[hotspot]";
  private final static String INTERFACES_WLAN_TEMPLATE = "interfaces[wlan]";

  private final static File HOST_APD_FILE = new File(HOST_APD_CONF);
  private final static File HOST_APD_BACKUP_FILE = new File(HOST_APD_CONF + ".bak");
  private final static File INTERFACES_FILE = new File(INTERFACES_CONF);
  private final static File INTERFACES_BACKUP_FILE = new File(INTERFACES_CONF + ".bak");

  private String ssid;
  private String password;
  private String staticIp;

  private Thread hotSpotThread;
  private SystemCommandExecutor hotSpotExecutor;
  private TemplateSet set;
  private HttpServer wlanSetupServer;

  public HotSpotImpl(String ssid, String password, String staticIp) throws IllegalAccessException {
    this.ssid = ssid;
    this.password = password;
    this.staticIp = staticIp;

    TemplateService templateService = Callete.getTemplateService();
    set = templateService.createTemplateSetFromPackage(HotSpotImpl.class, "");

    if(password.length() < 8) {
      throw new IllegalAccessException("Password should be at least 8 characters long.");
    }
  }

  @Override
  public boolean install() {
    try {
      LOG.info("Installing hot spot '" + ssid + "' with static ip " + staticIp);
      FileUtils.createBackup(INTERFACES_FILE, INTERFACES_BACKUP_FILE);
      FileUtils.createBackup(HOST_APD_FILE, HOST_APD_BACKUP_FILE);

      //write interface configuration
      INTERFACES_FILE.delete();
      LOG.info("Writing new " + INTERFACES_CONF + " configuration");
      set.renderTemplate(INTERFACES_HOTSPOT_TEMPLATE, new HotSpotRepresentation(staticIp, SUBNET_MASK), INTERFACES_FILE);

      //write host apd conf
      HOST_APD_FILE.delete();
      LOG.info("Writing new " + HOST_APD_CONF + " configuration");
      set.renderTemplate(HOST_APD_TEMPLATE, new HostAPDRepresentation(ssid, password), HOST_APD_FILE);

      LOG.info("HotSpot setup finished.");
    } catch (Exception e) {
      LOG.error("Error installing hot spot: " + e.getMessage(), e);
      try {
        FileUtils.restoreBackup(INTERFACES_BACKUP_FILE, INTERFACES_FILE);
        FileUtils.restoreBackup(HOST_APD_BACKUP_FILE, HOST_APD_FILE);
      } catch (IOException e1) {
        LOG.error("Error restoring backup file " + INTERFACES_BACKUP_FILE.getAbsolutePath() + ": " + e.getMessage(), e);
      }
      return false;
    }

    return true;
  }

  @Override
  public boolean start() {
    Callete.getNetworkService().restartHostAPD();
    Callete.getNetworkService().restartDNSMasq();
    Callete.getNetworkService().stopWPAAction();
    Callete.getNetworkService().restartNetworking();
    Callete.getNetworkService().reconnectToWlan();

    hotSpotThread = new Thread() {
      @Override
      public void run() {
        Thread.currentThread().setName("Hot Spot '" + ssid + "' runner");
        hotSpotExecutor = new SystemCommandExecutor(Splitter.on(" ").splitToList(RUN_CMD));
        try {
          hotSpotExecutor.executeCommand();
        } catch (Exception e) {
          LOG.error("Error in hot spot thread: " + e.getMessage(), e);
        }
      }
    };
    hotSpotThread.start();
    return true;
  }

  @Override
  public boolean uninstall() {
    LOG.info("Terminating hostapd process");
    if(hotSpotExecutor != null) {
      hotSpotExecutor.killProcess();
    }
    LOG.info("Stopping WLAN setup server");
    if(wlanSetupServer != null) {
      wlanSetupServer.stop();
    }
    
    try {
      INTERFACES_FILE.delete();

      LOG.info("Restoring original interfaces configuration.");
      set.renderTemplate(INTERFACES_WLAN_TEMPLATE, new WLANRepresentation(), INTERFACES_FILE);

      Callete.getNetworkService().restartNetworking();
      Callete.getNetworkService().reconnectToWlan();
    } catch (Exception e) {
      LOG.error("Error un-installing hot spot: " + e.getMessage(), e);
      return false;
    }
    return true;
  }

  @Override
  public void startWLANConfigService(File resourceFolder, int port) {
    wlanSetupServer = Callete.getHttpService().startServer(staticIp, port, resourceFolder, new String[]{"callete.api.services.impl.network"});
  }

}
