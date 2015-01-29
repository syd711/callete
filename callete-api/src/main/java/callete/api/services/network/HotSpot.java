package callete.api.services.network;

import org.glassfish.grizzly.http.server.HttpServer;

import java.io.File;

/**
 * The Hotspot instance.
 */
public interface HotSpot {

  /**
   * Changes the system setup for being a hot spot.
   */
  boolean install();

  /**
   * Starts the hot spot
   */
  boolean start();

  /**
   * Stops the hot spot, uninstalls the networks scripts
   */
  boolean uninstall();

  /**
   * Starts a WLAN settings service including REST resource and frontend
   * @param resourceDirectory the directory to lookup the UI resources
   * @param port
   */
  void startWLANConfigService(File resourceDirectory, int port);
}
