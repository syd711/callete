package callete.api.services.gpio;

/**
 * Interface to be implemented by a delegating implementation
 */
public interface DigitalOutputPin extends GPIOComponent {

  /**
   * Sets the pin to the PinState.LOW
   */
  void low();

  /**
   * Sets the pin to the PinState.HIGH
   */
  void high();

  /**
   * Toggles the high/low state
   */
  void toggle();

  /**
   * Returns the pin this pin is connected to.
   */
  int getPin();

  /**
   * Adds a listener to listen for pin state changes.
   */
  void addPinStateChangeListener(PinStateChangeListener listener);
}
