package callete.api.services.gpio;

/**
 * Listener to be implemented by clients that want to listen on button pushed events.
 */
public interface PushListener {

  /**
   * Fired when a push button is pushed.
   *
   * @param e The event contains the event source and the pin id the button is connected to.
   */
  void pushed(PushEvent e);


  /**
   * Returns the amount of milliseconds the button should have been pressed before
   * the push event is fired.
   *
   * @return the button debounce value in milliseconds.
   */
  long getPushDebounceMillis();

  /**
   * Returns the amount of milliseconds the button should have been pressed before
   * the push event for a long push is fired.
   *
   * @return the button debounce value in milliseconds.
   */
  long getLongPushDebounceMillis();
}
