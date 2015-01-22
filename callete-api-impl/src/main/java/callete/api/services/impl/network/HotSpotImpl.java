package callete.api.services.impl.network;

import callete.api.Callete;
import callete.api.services.impl.network.model.HostAPDRepresentation;
import callete.api.services.impl.network.model.HotSpotRepresentation;
import callete.api.services.impl.network.model.WLANRepresentation;
import callete.api.services.network.HotSpot;
import callete.api.services.network.WirelessNetwork;
import callete.api.services.template.TemplateService;
import callete.api.services.template.TemplateSet;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The hot spot implementation, implemented for the Raspi.
 */
public class HotSpotImpl implements HotSpot {
  private final static Logger LOG = LoggerFactory.getLogger(HotSpotImpl.class);

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
  
  private final static int MAX_WAIT_ATTEMPTS = 10;

  private String ssid;
  private String password;
  private String staticIp;
  private String subnetMask;
  
  private Thread hotSpotThread;
  private SystemCommandExecutor hotSpotExecutor;
  private TemplateSet set;

  public HotSpotImpl(String ssid, String password, String staticIp, String subnetMask) {
    this.ssid = ssid;
    this.password = password;
    this.staticIp = staticIp;
    this.subnetMask = subnetMask;

    TemplateService templateService = Callete.getTemplateService();
    set = templateService.createTemplateSetFromPackage(HotSpotImpl.class, "");
  }

  @Override
  public boolean install() {
    try {
      LOG.info("Installing hot spot '" + ssid + "' with static ip " + staticIp);
      createBackup(INTERFACES_FILE, INTERFACES_BACKUP_FILE);
      createBackup(HOST_APD_FILE, HOST_APD_BACKUP_FILE);

      //write interface configuration
      INTERFACES_FILE.delete();
      LOG.info("Writing new " + INTERFACES_CONF + " configuration");
      set.renderTemplate(INTERFACES_HOTSPOT_TEMPLATE, new HotSpotRepresentation(staticIp, subnetMask), INTERFACES_FILE);
      
      //write host apd conf
      HOST_APD_FILE.delete();
      LOG.info("Writing new " + HOST_APD_CONF + " configuration");
      set.renderTemplate(HOST_APD_TEMPLATE, new HostAPDRepresentation(ssid, password), HOST_APD_FILE);
      
      LOG.info("HotSpot setup finished.");
    }
    catch(Exception e) {
      LOG.error("Error installing hot spot: " + e.getMessage(), e);
      try {
        restoreBackup(INTERFACES_BACKUP_FILE, INTERFACES_FILE);
        restoreBackup(HOST_APD_BACKUP_FILE, HOST_APD_FILE);
      } catch (IOException e1) {
        LOG.error("Error restoring backup file " + INTERFACES_BACKUP_FILE.getAbsolutePath() + ": " + e.getMessage(), e);
      }
      return false;
    }
    
    return true;
  }

  @Override
  public boolean start() {
    Callete.getNetworkService().restartNetworking();
    
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
    waitUntilAvailable();
    return true;
  }

  @Override
  public boolean uninstall() {
    hotSpotExecutor.killProcess();
    try {
      INTERFACES_FILE.delete();
      
      LOG.info("Restoring original interfaces configuration.");
      set.renderTemplate(INTERFACES_WLAN_TEMPLATE, new WLANRepresentation(), INTERFACES_FILE);

      Callete.getNetworkService().restartNetworking();
    }
    catch(Exception e) {
      LOG.error("Error un-installing hot spot: " + e.getMessage(), e);
      return false;
    }
    return true;
  }

  //------------------ Helper ---------------------------------
  
  private void startConfigServer() {
    HttpServer httpServer = Callete.getHttpService().startServer("192.168.2.10", 8088, new File("./"), new String[]{"callete.api.services.impl.network"});


  }

  private void waitUntilAvailable() {
    int attempts = 1;
    try {
      LOG.info("Waiting until hot spot '" + ssid + "' becomes available...");
      List<WirelessNetwork> wirelessNetworks = Callete.getNetworkService().scanWirelessNetworks();
      LOG.info("Scanned " + wirelessNetworks.size() + " wireless networks");
      for(WirelessNetwork network : wirelessNetworks) {
        if(network.getSSID().equalsIgnoreCase(ssid)) {
          LOG.info("Found hot spot: " + network);
          return;
        }
      }

      LOG.info("Waited " + attempts*2 + "seconds...");
      Thread.sleep(2000);
      attempts++;
      if(attempts >= MAX_WAIT_ATTEMPTS) {
        LOG.error("Failed to find hot spot network '" + ssid + "', uninstalling hot spot...");

        uninstall();
      }
    } catch (InterruptedException e) {
      LOG.error("Error waiting for hotspot network: " + e.getMessage(), e);
    }
  }

  private void createBackup(File f, File backup) throws IOException {
    LOG.info("Creating backup file " + f.getAbsolutePath());
    if(f.exists()) {
      if(backup.exists()) {
        backup.delete();
      }
      Files.copy(f, backup);
    }
  }

  private void restoreBackup(File backup, File f) throws IOException {
    LOG.info("Restoring backup file " + f.getAbsolutePath());
    if(backup.exists()) {
      if(f.exists()) {
        f.delete();
      }
      Files.copy(backup, f);
    }
  }
}
