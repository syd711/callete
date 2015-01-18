package callete.api.services.network;

/**
 * Enum that describes the different authentication methods of wireless networks.
 */
public enum NetworkAuthentication {
  WPA, WPA2, WEP, NONE;

  public static NetworkAuthentication getEnum(String value) {
    for(NetworkAuthentication v : values()) {
      if(v.toString().equalsIgnoreCase(value)) {
        return v;
      }
    }
    if(value.equals("Offen")) {
      return NONE;
    }
    if(value.contains("WPA2")) {
      return WPA2;
    }
    if(value.contains("WPA ")) {
      return WPA;
    }
    
    return null;
  }
}
