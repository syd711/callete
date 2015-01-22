package callete.api.services.impl.network.model;

/**
 * Pojo for ftl rendering.
 */
public class HotSpotRepresentation {
  private String address;
  private String netmask;

  public HotSpotRepresentation(String address, String netmask) {
    this.address = address;
    this.netmask = netmask;
  }

  public String getAddress() {
    return address;
  }

  public String getNetmask() {
    return netmask;
  }
}
