package callete.api.services.gpio;

/**
 * Listener to be used to listen on pin state changes.
 */
public interface PinStateChangeListener {

  /**
   * Event to be fired when the pin state has changed.
   */
  void pinStateChanged(PinStateChangeEvent event);
}
