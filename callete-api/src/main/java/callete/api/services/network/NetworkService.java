package callete.api.services.network;

import callete.api.services.Service;

import java.util.List;

/**
 * The network service allows to switch between hot spot and regular WLAN client mode. 
 * It is used to setup the WLAN connection for the client by using a hot spot connection.
 */
public interface NetworkService extends Service{

  /**
   * Creates a new temporary hotspot with the given parameters.
   * @param ssid the SSID of the wireless hot spot
   * @param password the password used for authentication to the hot spot
   * @param staticIp the static ip address the hot spot is available under
   */
  HotSpot createHotSpot(String ssid, String password, String staticIp) throws IllegalAccessException;

  /**
   * Returns the active hot spot or null if none has been started.
   */
  HotSpot getActiveHotSpot();
  
  /**
   * Returns a list of all wireless networks available.
   */
  List<WirelessNetwork> scanWirelessNetworks();

  /**
   * Overwrites the existing wpa_supplicant.conf if the system. 
   * We assume here that this conf file is linked in the default interfaces settings that
   * is restored onece the hotspot has been uninstalled.
   * @param network the network information to write into the conf file
   * @param password the password of the network
   */
  boolean updateWpaSupplicantConf(WirelessNetwork network, String password);

  /**
   * Restarts the networking service to apply changed settings.
   */
  void restartNetworking();

  /**
   * Will stop WPA for wlan0.
   */
  void stopWPAAction();

  /**
   * Restart of the hostapd service 
   */
  void restartHostAPD();

  /**
   * Restart of the dnsmasq service.
   */
  void restartDNSMasq();

  /**
   * Assumes that it is possible to reconnect via DHCP
   */
  void reconnectToWlan();

  /**
   * Pings the given URL to test if the internet connection is working.
   * @return true if the ping was successful.
   */
  boolean isOnline();
}
