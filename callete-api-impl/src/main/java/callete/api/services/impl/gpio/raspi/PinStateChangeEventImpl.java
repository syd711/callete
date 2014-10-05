package callete.api.services.impl.gpio.raspi;

import callete.api.services.gpio.PinState;
import callete.api.services.gpio.PinStateChangeEvent;

/**
 * Implements the Pin State Change Event interface for the Raspberry Pi GPIO API.
 */
public class PinStateChangeEventImpl implements PinStateChangeEvent {

  private PinState state;
  private Object source;
  private int pin;

  public PinStateChangeEventImpl(Object source, int pin, PinState state) {
    this.state = state;
    this.source = source;
    this.pin = pin;
  }

  @Override
  public Object getSource() {
    return source;
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public PinState getState() {
    return state;
  }
}
