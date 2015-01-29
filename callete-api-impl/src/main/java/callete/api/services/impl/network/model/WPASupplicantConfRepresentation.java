package callete.api.services.impl.network.model;

/**
 * POJO for template rendering
 */
public class WPASupplicantConfRepresentation {
  private String ssid;
  private String password;

  public WPASupplicantConfRepresentation(String ssid, String password) {
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
