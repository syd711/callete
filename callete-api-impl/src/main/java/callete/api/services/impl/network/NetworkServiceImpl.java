package callete.api.services.impl.network;

import callete.api.services.network.HotSpot;
import callete.api.services.network.NetworkService;
import callete.api.services.network.WirelessNetwork;

import java.util.List;

/**
 *
 */
public class NetworkServiceImpl implements NetworkService {
  @Override
  public HotSpot createHotSpot(String ssid, String username, String password, String staticIp, String subnetMask) {
    return null;
  }

  @Override
  public boolean isHotSpotAvailable(HotSpot hotSpot) {
    return false;
  }

  @Override
  public List<WirelessNetwork> scanWirelessNetworks() {
    return null;
  }
}
