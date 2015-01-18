package callete.api.services.network;

import callete.api.services.Service;

import java.util.List;

/**
 * The network service allows to switch between hot spot and regular WLAN client mode. 
 * It is used to setup the WLAN connection for the client by using a hot spot connection.
 */
public interface  NetworkService extends Service{

  HotSpot createHotSpot(String ssid, String username, String password, String staticIp, String subnetMask);

  /**
   * Returns true if the given hot spot is available.
   * @param hotSpot the hot spot the search for
   */
  boolean isHotSpotAvailable(HotSpot hotSpot);

  /**
   * Returns a list of all wireless networks available.
   */
  List<WirelessNetwork> scanWirelessNetworks();
}
