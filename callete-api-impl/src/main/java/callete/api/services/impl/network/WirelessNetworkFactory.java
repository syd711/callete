package callete.api.services.impl.network;

import callete.api.services.network.WirelessNetwork;
import callete.api.util.SystemUtils;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper to extract the wireless network information from a system command string.
 */
public class WirelessNetworkFactory {
  private final static String LINUX_WLAN_SCAN_CMD = "sudo iwlist wlan0 scan";
  private final static String WINDOWS_WLAN_SCAN_CMD = "netsh wlan show network";

  public static List<WirelessNetwork> createWirelessNetworkInfo() {
    if(SystemUtils.isWindows()) {
      return getWirelessNetworksForWindows();
    }

    return getWirelessNetworksForLinux();
  }

  private static List<WirelessNetwork> getWirelessNetworksForLinux() {
    List<WirelessNetwork> infos = new ArrayList<>();
    String result = SystemUtils.execute(Splitter.on(" ").splitToList(LINUX_WLAN_SCAN_CMD));
    List<String> cell = Splitter.on("Cell").splitToList(result);
    List<String> ssidStrings = cell.subList(1, cell.size());
    for(String cmd : ssidStrings) {
      WirelessNetwork info = WirelessNetworkImpl.forLinux(cmd);
      infos.add(info);
    }
    return infos;
  }

  private static List<WirelessNetwork> getWirelessNetworksForWindows() {
    List<WirelessNetwork> infos = new ArrayList<>();
    String result = SystemUtils.execute(Splitter.on(" ").splitToList(WINDOWS_WLAN_SCAN_CMD));
    List<String> cmdResults = Splitter.on("SSID").splitToList(result);
    List<String> ssidStrings = cmdResults.subList(1, cmdResults.size());

    for(String cmd : ssidStrings) {
      WirelessNetwork info = WirelessNetworkImpl.forWindows(cmd);
      infos.add(info);
    }
    return infos;
  }
}
