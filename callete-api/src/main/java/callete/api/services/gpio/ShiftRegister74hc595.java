package callete.api.services.gpio;

/**
 * Interface for handling a shift register.
 */
public interface ShiftRegister74hc595 extends GPIOComponent {

  /**
   * Sets the given pin to high or log.
   * @param pin the number of the pin. This is not the actual wiring pi pin id or something
   *            but just the plain number of the register, starting from zero to n*8 depending 
   *            on the number of shift registers.
   * @param pinState the state that should be applied to the pin.
   */
  void setRegisterPin(int pin, PinState pinState);

  /**
   * Sets all pins to LOW.
   */
  void clearRegisters();

  /**
   * Must be called once the individual state for each register has been set.
   */
  void writeRegisters();
}
