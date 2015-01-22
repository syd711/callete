package callete.api.services.impl.network.model;

/**
 * Pojo for ftl rendering.
 */
public class HostAPDRepresentation {
  private String ssid;
  private String password;

  public HostAPDRepresentation(String ssid, String password) {
    this.ssid = ssid;
    this.password = password;
  }

  public String getSsid() {
    return ssid;
  }

  public String getPassword() {
    return password;
  }
}
