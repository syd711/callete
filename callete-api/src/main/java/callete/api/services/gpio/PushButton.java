package callete.api.services.gpio;

/**
 * Interface to be implemented by GPIO flavors.
 */
public interface PushButton {

  /**
   * Registers a listener that is fired if the button is pressed.
   * @see callete.api.services.gpio.PushListener
   */
  public void addPushListener(PushListener listener);

  /**
   * Returns the pin number the button is connected to.
   */
  public int getPin();

  /**
   * The name that has been set for this button.
   */
  public String getName();
}
