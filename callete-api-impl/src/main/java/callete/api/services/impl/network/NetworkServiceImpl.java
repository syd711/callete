package callete.api.services.impl.network;

import callete.api.Callete;
import callete.api.services.impl.network.model.HotSpotRepresentation;
import callete.api.services.impl.network.model.WPASupplicantConfRepresentation;
import callete.api.services.network.HotSpot;
import callete.api.services.network.NetworkAuthentication;
import callete.api.services.network.NetworkService;
import callete.api.services.network.WirelessNetwork;
import callete.api.services.template.TemplateService;
import callete.api.services.template.TemplateSet;
import callete.api.util.FileUtils;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 *
 */
public class NetworkServiceImpl implements NetworkService {
  private final static Logger LOG = LoggerFactory.getLogger(NetworkServiceImpl.class);

  private static final String NETWORK_RESTART_CMD = "sudo service networking restart";
  private static final String RECONNECT_WLAN_CMD = "sudo ifup wlan0";
  private static final String WPA_STOP_CMD = "sudo wpa_action wlan0 stop";
  private static final String HOST_APD_RESTART_CMD = "sudo service hostapd restart";
  private static final String DNSMASQ_RESTART_CMD = "sudo service dnsmasq restart";

  private final static String WPA_SUPPLICANT_WEP_TEMPLATE = "wpa_supplicant.conf[wep]";
  private final static String WPA_SUPPLICANT_WAP_TEMPLATE = "wpa_supplicant.conf[wpa]";
  private final static File WPA_SUPPLICANT_FILE = new File("/etc/wpa_supplicant/wpa_supplicant.conf");
  private final static File WPA_SUPPLICANT_BACKUP_FILE = new File("/etc/wpa_supplicant/wpa_supplicant.conf.bak");
  
  private HotSpot activeHotSpot;

  @Override
  public HotSpot createHotSpot(String ssid, String password, String staticIp) throws IllegalAccessException {
    activeHotSpot = new HotSpotImpl(ssid, password, staticIp);
    return activeHotSpot;
  }
  
  @Override
  public boolean updateWpaSupplicantConf(WirelessNetwork network, String password) {
    try {
      TemplateService templateService = Callete.getTemplateService();
      TemplateSet set = templateService.createTemplateSetFromPackage(NetworkServiceImpl.class, "");
      
      FileUtils.createBackup(WPA_SUPPLICANT_FILE, WPA_SUPPLICANT_BACKUP_FILE);
      String template = WPA_SUPPLICANT_WAP_TEMPLATE;
      if(network.getAuthentication().equals(NetworkAuthentication.WEP)) {
        template = WPA_SUPPLICANT_WEP_TEMPLATE;
      }

      //write configuration
      WPA_SUPPLICANT_FILE.delete();
      LOG.info("Writing new " + WPA_SUPPLICANT_FILE.getAbsolutePath() + " configuration");
      set.renderTemplate(template, new WPASupplicantConfRepresentation(network.getSSID(), password), WPA_SUPPLICANT_FILE);
      LOG.info("Written " + WPA_SUPPLICANT_FILE.getAbsolutePath());
      return true;
    }
    catch (Exception e) {
      LOG.error("Error updating WPA supplicant configuration: " + e.getMessage(), e);
      try {
        FileUtils.restoreBackup(WPA_SUPPLICANT_BACKUP_FILE, WPA_SUPPLICANT_FILE);
      } catch (IOException e1) {
        LOG.error("Error restoring WAP supplicant configuration: " + e.getMessage(), e);
      }
    }
    return false;
  }

  @Override
  public HotSpot getActiveHotSpot() {
    return activeHotSpot;
  }

  @Override
  public List<WirelessNetwork> scanWirelessNetworks() {
    return WirelessNetworkFactory.createWirelessNetworkInfo();
  }

  @Override
  public boolean isOnline() {
    try {
      final URL url = new URL("http://www.google.com");
      final URLConnection conn = url.openConnection();
      InputStream inputStream = conn.getInputStream();
      inputStream.close();
      return true;
    }
    catch (Exception e) {
      LOG.warn("Ping to google failed.");
    }
    
    return false;
  }

  @Override
  public void restartNetworking() {
    LOG.info("Restarting networking");
    executeCommand(NETWORK_RESTART_CMD);
  }

  @Override
  public void stopWPAAction() {
    LOG.info("Stopping WPA");
    executeCommand(WPA_STOP_CMD);
  }

  @Override
  public void restartHostAPD() {
    LOG.info("Restarting hostapd service");
    executeCommand(HOST_APD_RESTART_CMD);
  }

  @Override
  public void restartDNSMasq() {
    LOG.info("Restarting DNS masq service");
    executeCommand(DNSMASQ_RESTART_CMD);
  }

  @Override
  public void reconnectToWlan() {
    LOG.info("Reassigning IP");
    executeCommand(RECONNECT_WLAN_CMD);
  }

  //----------------- Helper ------------------------------------

  private void executeCommand(String cmd) {
    SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList(cmd));
    try {
      executor.enableLogging(true);
      executor.executeCommand();

      String error = executor.getStandardErrorFromCommand().toString();
      if(error != null && error.trim().length() > 0) {
        LOG.error("Error executing network command: " + error);
      }
    } catch (Exception e) {
      LOG.error("Failed to restart networking: " + e.getMessage(), e);
    }
  }
}
