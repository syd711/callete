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
   * @param subnetMask the subnet mask
   */
  HotSpot createHotSpot(String ssid, String password, String staticIp, String subnetMask);

  /**
   * Returns a list of all wireless networks available.
   */
  List<WirelessNetwork> scanWirelessNetworks();

  /**
   * Restarts the networking service to apply changed settings.
   */
  void restartNetworking();

  /**
   * Pings the given URL to test if the internet connection is working.
   * @param url the test URL
   * @return true if the ping was successful.
   */
  boolean ping(String url);
}
