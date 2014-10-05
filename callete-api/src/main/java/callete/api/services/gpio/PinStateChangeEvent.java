package callete.api.services.gpio;

/**
 * Interface to be implemented by the concrete embedded flavour.
 */
public interface PinStateChangeEvent {

  /**
   * Returns the source GPIO component that fired this event.
   */
  Object getSource();

  /**
   * Returns the pin number ob the push button
   */
  int getPin();

  /**
   * Returns the updated pin state of the pin that fired the event.
   */
  PinState getState();
}
