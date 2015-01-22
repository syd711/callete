package callete.api.services.impl.network;

import callete.api.services.network.HotSpot;
import callete.api.services.network.NetworkService;
import callete.api.services.network.WirelessNetwork;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class NetworkServiceImpl implements NetworkService {
  private final static Logger LOG = LoggerFactory.getLogger(NetworkServiceImpl.class);

  public static final String NETWORK_RESTART_CMD = "sudo service networking restart";

  @Override
  public HotSpot createHotSpot(String ssid, String password, String staticIp, String subnetMask) {
    return new HotSpotImpl(ssid, password, staticIp, subnetMask);
  }

  @Override
  public List<WirelessNetwork> scanWirelessNetworks() {
    return WirelessNetworkFactory.createWirelessNetworkInfo();
  }

  @Override
  public boolean ping(String url) {
    return false;
  }

  @Override
  public void restartNetworking() {
    LOG.info("Restarting networking.");
    SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList(NETWORK_RESTART_CMD));
    try {
      executor.executeCommand();

      String error = executor.getStandardErrorFromCommand().toString();
      if(error != null && error.trim().length() > 0) {
        LOG.error("Error executing restart command: " + error);
      }
    } catch (Exception e) {
      LOG.error("Failed to restart networking: " + e.getMessage(), e);
    }
  }
}
