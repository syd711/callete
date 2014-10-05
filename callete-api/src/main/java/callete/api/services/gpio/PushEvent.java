package callete.api.services.gpio;

/**
 * Interface to be implemented by the concrete embedded flavour.
 */
public interface PushEvent {

  /**
   * Returns the source GPIO component that fired this event.
   */
  Object getSource();

  /**
   * Returns the pin number ob the push button
   */
  int getPin();
}
