package callete.api.services.network;

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
}
