package callete.api.services.network;

/**
 * Describes the properties of a wireless network, like it's encryption and SSID.
 */
public interface WirelessNetwork {

  /**
   * Returns the SSID of the network. 
   */
  String getSSID();

  /**
   * Returns the authentication type of the network.
   */
  NetworkAuthentication getAuthentication();

  /**
   * Returns the signal strength, value from 0-100.
   */
  int getSignalStrength();

  /**
   * Returns the signal quality, value from 0-100  
   */
  int getSignalQuality();
}
