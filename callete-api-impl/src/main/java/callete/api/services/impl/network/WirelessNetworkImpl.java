package callete.api.services.impl.network;

import callete.api.services.network.NetworkAuthentication;
import callete.api.services.network.WirelessNetwork;


/**
 * Contains
 */
public class WirelessNetworkImpl implements WirelessNetwork {
  private String ssid;
  private NetworkAuthentication authentication;
  private int signalStrength = -1;
  private int signalQuality = -1;
  
  public WirelessNetworkImpl(String ssid, NetworkAuthentication authentication, int signalQuality, int signalStrength) {
    this.ssid =ssid;
    this.authentication = authentication;
    this.signalQuality = signalQuality;
    this.signalStrength = signalStrength;
  }

  /**
   * <code>
   *  SSID 6 : KD WLAN Hotspot
   *  Netzwerktyp             : Infrastruktur
   *  Authentifizierung       : Offen
   *  Verschlüsselung         : Keine 
   * </code>
   */
  public static WirelessNetwork forWindows(String cmd) {
    cmd = "SSID " + cmd;
    
    String ssid = null;
    NetworkAuthentication authentication = null;
    String[] split = cmd.split("\n");
    for(String line : split) {
      String[] row = line.split(":");
      String rowKey = row[0].trim();
      String rowValue = row[1].trim();
      
      if(rowKey.contains("SSID")) {
        ssid = rowValue;
      }
      if(rowKey.contains("Authent")) {
        authentication = NetworkAuthentication.getEnum(rowValue);
      }
      if(authentication != null && authentication.equals(NetworkAuthentication.NONE) && rowKey.contains("Verschl")) {
        authentication = NetworkAuthentication.getEnum(rowValue);
      }
    }
    
    if(authentication == null) {
      authentication = NetworkAuthentication.NONE;
    }
    
    return new WirelessNetworkImpl(ssid, authentication, -1, -1);
  }
  
  /**
   * <code>
   *   wlan0     Scan completed :
   Cell 01 - Address: 0E:96:D7:B1:6E:C7
   ESSID:"BASTINDA"
   Protocol:IEEE 802.11bg
   Mode:Master
   Frequency:2.412 GHz (Channel 1)
   Encryption key:on
   Bit Rates:54 Mb/s
   Quality=100/100  Signal level=100/100
   Cell 02 - Address: 00:24:FE:08:D0:0D
   ESSID:"BASTINDA"
   Protocol:IEEE 802.11bg
   Mode:Master
   Frequency:2.412 GHz (Channel 1)
   Encryption key:on
   Bit Rates:54 Mb/s
   Quality=100/100  Signal level=47/100
   Cell 03 - Address: 9C:C7:A6:B6:CB:07
   ESSID:"Sillemstrasse"
   Protocol:IEEE 802.11bgn
   Mode:Master
   Frequency:2.412 GHz (Channel 1)
   Encryption key:on
   Bit Rates:300 Mb/s
   Extra:wpa_ie=dd160050f20101000050f20201000050f20201000050f202
   IE: WPA Version 1
   Group Cipher : TKIP
   Pairwise Ciphers (1) : TKIP
   Authentication Suites (1) : PSK
   Extra:rsn_ie=30140100000fac020100000fac040100000fac020000
   IE: IEEE 802.11i/WPA2 Version 1
   Group Cipher : TKIP
   Pairwise Ciphers (1) : CCMP
   Authentication Suites (1) : PSK
   Quality=100/100  Signal level=60/100

   * </code>
   */
  public static WirelessNetwork forLinux(String cmd) {
    String ssid = null;
    NetworkAuthentication authentication = null;
    int signalStrength = -1;
    int signalQuality = -1;

    String[] split = cmd.split("\n");
    for(String line : split) {
      String[] row = line.split(":");
      if(row.length >= 2) {
        String rowKey = row[0].trim();
        String rowValue = row[1].trim();

        if(rowKey.equals("ESSID")) {
          ssid = rowValue.replaceAll("\"", "");
        }
        if(line.contains("Encryption key:off")) {
          authentication = NetworkAuthentication.NONE;
        }
        if(rowKey.equals("IE")) {
          NetworkAuthentication secondIE = NetworkAuthentication.getEnum(rowValue);
          if(secondIE != null) {
            authentication = secondIE;
          }
        }
      }
      else {
        if(line.contains("Quality")) {
          String[] signal = line.trim().split(" Signal ");
          String[] quality = signal[0].split("=");
          signalQuality = Integer.parseInt(quality[1].trim().split("/")[0].trim());

          String[] strength = signal[1].split("=");
          signalStrength = Integer.parseInt(strength[1].trim().split("/")[0].trim());
        }
      }
    }
    
    if(authentication == null) {
      authentication = NetworkAuthentication.WEP;
    }

    return new WirelessNetworkImpl(ssid, authentication, signalQuality, signalStrength);
  }

  @Override
  public String getSSID() {
    return ssid;
  }

  @Override
  public NetworkAuthentication getAuthentication() {
    return authentication;
  }

  @Override
  public int getSignalStrength() {
    return signalStrength;
  }

  @Override
  public int getSignalQuality() {
    return signalQuality;
  }

  @Override
  public String toString() {
    return "Wireless Network '" + ssid + "', Authentication: " + authentication + ", signal strength: " 
        + signalStrength + " signal quality: " + signalQuality;
  }
}
