package callete.api.services.impl.network.model;

import callete.api.services.network.WirelessNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for REST
 */
public class NetworksRepresentation {
  private List<WirelessNetwork> items = new ArrayList<>();

  public List<WirelessNetwork> getItems() {
    return items;
  }

  public void setItems(List<WirelessNetwork> items) {
    this.items = items;
  }
}
