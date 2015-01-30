package callete.api.services.impl.network;

import callete.api.Callete;
import callete.api.services.impl.network.model.NetworksRepresentation;
import callete.api.services.impl.network.model.SystemCommandRepresentation;
import callete.api.services.network.WirelessNetwork;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource used to configure a WLAN connection
 */
@Path("/hotspot")
@Produces(MediaType.APPLICATION_JSON)
public class WlanSetupResource {
  private final static Logger LOG = LoggerFactory.getLogger(WlanSetupResource.class);
  
  private static List<WirelessNetwork> lastScan = new ArrayList<>();

  @GET
  @Path("/networks")
  public NetworksRepresentation getNetworks() {
    lastScan = Callete.getNetworkService().scanWirelessNetworks();
    NetworksRepresentation networks = new NetworksRepresentation();
    networks.setItems(lastScan);
    return networks;
  }

  @GET
  @Path("/shutdown")
  public NetworksRepresentation reboot() {    
    SystemCommandRepresentation result = new SystemCommandRepresentation();
    
    try {
      LOG.info("Uninstalling Hotspot");
      Callete.getNetworkService().getActiveHotSpot().uninstall();

      LOG.info("Rebooting System");
      Callete.getSystemService().reboot();
    } catch (Exception e) {
      result.setError(e.getMessage());
    }
    //return something
    lastScan = Callete.getNetworkService().scanWirelessNetworks();
    NetworksRepresentation networks = new NetworksRepresentation();
    networks.setItems(lastScan);
    return networks;
  }

  @POST
  @Path("/save")
  public SystemCommandRepresentation save(@FormParam("ssid") String ssid, @FormParam("password") String password) {
    LOG.info("Saving WLAN '" + ssid + "'");
    SystemCommandRepresentation result = new SystemCommandRepresentation();
    for(WirelessNetwork network : lastScan) {
      if(network.getSSID().equalsIgnoreCase(ssid)) {
        LOG.info("Found network configuration for SSID '" + ssid + "'");
        Callete.getNetworkService().updateWpaSupplicantConf(network, password);
        return result;
      }
    }
    LOG.error("No network configuration found fo SSID '" + ssid + "'");
    return result;
  }


  @POST
  @Path("/command")
  public SystemCommandRepresentation executeCommand(@FormParam("command") String command) {
    SystemCommandRepresentation result = new SystemCommandRepresentation();
    if(StringUtils.isEmpty(command)) {
      LOG.error("Could not execute empty system command");
      result.setError("Could not execute empty system command");
      return result;
    }

    SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList(command));
    try {
      executor.executeCommand();
      result.setError(executor.getStandardErrorFromCommand().toString());
      result.setOutput(executor.getStandardOutputFromCommand().toString());
    } catch (Exception e) {
      LOG.error("Error executing system command for WLAN setup: " + e.getMessage(), e);
      result.setError("Exception executing command: " + e.getMessage());
    }
    return result;
  }

  @GET
  @Path("/log")
  public SystemCommandRepresentation loadLogs() {
    SystemCommandRepresentation result = new SystemCommandRepresentation();
    
    String command = "sudo cat /home/pi/callete-deployment/*.log";
    SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList(command));
    try {
      executor.executeCommand();
      result.setOutput(executor.getStandardOutputFromCommand().toString());
      if(StringUtils.isEmpty(result.getOutput())) {
        result.setOutput(executor.getStandardErrorFromCommand().toString());
      }
    } catch (Exception e) {
      LOG.error("Error executing log read command: " + e.getMessage(), e);
      result.setError("Exception reading logs: " + e.getMessage());
    }
    return result;
  }  
}
