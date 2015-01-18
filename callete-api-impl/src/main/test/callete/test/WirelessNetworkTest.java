package callete.test;

import callete.api.services.impl.network.WirelessNetworkImpl;
import callete.api.services.network.WirelessNetwork;
import callete.api.util.SystemUtils;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;

/**
 * Created by Matthias on 18.01.2015.
 */
public class WirelessNetworkTest {
  
  @Test
  public void testLinuxScan() {
    String output = "wlan0     Scan completed :\n" +
        "          Cell 01 - Address: 0E:96:D7:B1:6E:C7\n" +
        "                    ESSID:\"BASTINDA\"\n" +
        "                    Protocol:IEEE 802.11bg\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:54 Mb/s\n" +
        "                    Quality=100/100  Signal level=100/100\n" +
        "          Cell 02 - Address: 00:24:FE:08:D0:0D\n" +
        "                    ESSID:\"BASTINDA\"\n" +
        "                    Protocol:IEEE 802.11bg\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:54 Mb/s\n" +
        "                    Quality=100/100  Signal level=47/100\n" +
        "          Cell 03 - Address: 9C:C7:A6:B6:CB:07\n" +
        "                    ESSID:\"Sillemstrasse\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:wpa_ie=dd160050f20101000050f20201000050f20201000050f202\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac020100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Quality=100/100  Signal level=60/100\n" +
        "          Cell 04 - Address: 82:C7:A6:B6:CB:07\n" +
        "                    ESSID:\"fishNet! Gast\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:wpa_ie=dd160050f20101000050f20201000050f20201000050f202\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac020100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Quality=100/100  Signal level=61/100\n" +
        "          Cell 05 - Address: 88:03:55:58:85:C9\n" +
        "                    ESSID:\"WLAN-308743\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:144 Mb/s\n" +
        "                    Extra:rsn_ie=30140100000fac040100000fac040100000fac020c00\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : CCMP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    IE: Unknown: DD310050F204104A0001101044000102104700109164127AE863A88098B3198E5D279EE5103C0001031049000600372A000120\n" +
        "                    Quality=77/100  Signal level=43/100\n" +
        "          Cell 06 - Address: 02:26:4D:8D:55:AE\n" +
        "                    ESSID:\"MieziLAN\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.412 GHz (Channel 1)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:wpa_ie=dd1c0050f20101000050f20202000050f2020050f20401000050f2020000\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : TKIP CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30180100000fac020200000fac02000fac040100000fac020100\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : TKIP CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                       Preauthentication Supported\n" +
        "                    IE: Unknown: DD0E0050F204104A0001101044000102\n" +
        "                    Quality=100/100  Signal level=43/100\n" +
        "          Cell 07 - Address: 00:1C:4A:13:C5:9E\n" +
        "                    ESSID:\"Theodor Wlan Adorno\"\n" +
        "                    Protocol:IEEE 802.11bg\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.437 GHz (Channel 6)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:54 Mb/s\n" +
        "                    Extra:wpa_ie=dd180050f20101000050f20201000050f20201000050f2020000\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac020100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Quality=36/100  Signal level=61/100\n" +
        "          Cell 08 - Address: 00:1C:4A:A0:EF:AF\n" +
        "                    ESSID:\"Sch\\xF6ner_Oktober\"\n" +
        "                    Protocol:IEEE 802.11bg\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.437 GHz (Channel 6)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:54 Mb/s\n" +
        "                    Extra:wpa_ie=dd180050f20101000050f20201000050f20201000050f2020000\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac020100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Quality=48/100  Signal level=49/100\n" +
        "          Cell 09 - Address: 18:83:BF:70:B9:6A\n" +
        "                    ESSID:\"EasyBox-70B946\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.427 GHz (Channel 4)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:144 Mb/s\n" +
        "                    Extra:wpa_ie=dd1c0050f20101000050f20202000050f2020050f20401000050f2020000\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : TKIP CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30180100000fac020200000fac02000fac040100000fac020100\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : TKIP CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                       Preauthentication Supported\n" +
        "                    IE: Unknown: DD0E0050F204104A0001101044000102\n" +
        "                    Quality=65/100  Signal level=42/100\n" +
        "          Cell 10 - Address: 06:24:A5:BD:06:68\n" +
        "                    ESSID:\"BUFFALO-BD0668\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.437 GHz (Channel 6)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:130 Mb/s\n" +
        "                    Extra:wpa_ie=dd1a0050f20101000050f20202000050f2040050f20201000050f202\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : CCMP TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30180100000fac020200000fac04000fac020100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (2) : CCMP TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Quality=18/100  Signal level=26/100\n" +
        "          Cell 11 - Address: 00:26:5B:B1:79:C8\n" +
        "                    ESSID:\"HITRON-79C0\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.462 GHz (Channel 11)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:wpa_ie=dd160050f20101000050f20401000050f20401000050f202\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : CCMP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac040100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : CCMP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    IE: Unknown: DD9D0050F204104A0001101044000101103B000103104700102880288028801880A88000265BB179C81021001852616C696E6B20546563686E6F6C6F67792C20436F72702E1023001C52616C696E6B20576972656C6573732041636365737320506F696E74102400065254323836301042000831323334353637381054000800060050F20400011011000952616C696E6B41505310080002210C103C000101\n" +
        "                    Quality=77/100  Signal level=66/100\n" +
        "          Cell 12 - Address: 00:26:5B:B1:79:CA\n" +
        "                    ESSID:\"KD WLAN Hotspot+\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.462 GHz (Channel 11)\n" +
        "                    Encryption key:off\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Quality=91/100  Signal level=68/100\n" +
        "          Cell 13 - Address: 24:65:11:81:42:BA\n" +
        "                    ESSID:\"FRITZ!Box 7312\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.462 GHz (Channel 11)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:wpa_ie=dd160050f20101000050f20201000050f20201000050f202\n" +
        "                    IE: WPA Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : TKIP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    Extra:rsn_ie=30140100000fac020100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : TKIP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    IE: Unknown: DD1D0050F204104A0001101044000102103C0001031049000600372A000120\n" +
        "                    Quality=100/100  Signal level=43/100\n" +
        "          Cell 14 - Address: 00:1C:28:A4:A2:EF\n" +
        "                    ESSID:\"o2-WLAN35\"\n" +
        "                    Protocol:IEEE 802.11bgn\n" +
        "                    Mode:Master\n" +
        "                    Frequency:2.472 GHz (Channel 13)\n" +
        "                    Encryption key:on\n" +
        "                    Bit Rates:300 Mb/s\n" +
        "                    Extra:rsn_ie=30140100000fac040100000fac040100000fac020000\n" +
        "                    IE: IEEE 802.11i/WPA2 Version 1\n" +
        "                        Group Cipher : CCMP\n" +
        "                        Pairwise Ciphers (1) : CCMP\n" +
        "                        Authentication Suites (1) : PSK\n" +
        "                    IE: Unknown: DDA70050F204104A0001101044000102103B000103104700102880288028801880A880001C28A4A2EF1021001852616C696E6B20546563686E6F6C6F67792C20436F72702E1023001C52616C696E6B20576972656C6573732041636365737320506F696E74102400065254323836301042000831323334353637381054000800060050F20400011011000952616C696E6B41505310080002210C103C0001011049000600372A000120\n" +
        "                    Quality=72/100  Signal level=52/100\n";

    String result = output;
    List<String> cell = Splitter.on("Cell").splitToList(result);
    List<String> ssidStrings = cell.subList(1, cell.size());
    for(String cmd : ssidStrings) {
      WirelessNetwork info = WirelessNetworkImpl.forLinux(cmd);
      System.out.println(info);
    }
  }
}
