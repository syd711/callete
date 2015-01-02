package callete.api.services.impl.simulator;

import callete.api.services.gpio.DigitalOutputPin;
import callete.api.services.gpio.PinState;
import callete.api.services.gpio.PinStateChangeListener;
import callete.api.services.impl.gpio.raspi.PinStateChangeEventImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Raspberry Pi implementation of a digital output pin.
 */
public class SimulatorDigitalOutputPin implements DigitalOutputPin {
  private int pin;
  private boolean high = false;
  private String name;
  private List<PinStateChangeListener> stateEventListeners = new ArrayList<>();

  public SimulatorDigitalOutputPin(int pin, String name, PinState pinState) {
    this.pin = pin;
    this.name = name;
    this.high = pinState.equals(PinState.HIGH);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void low() {
    this.high = false;
    fireStateChangeEvent();
  }

  @Override
  public void high() {
    this.high = true;
    fireStateChangeEvent();
  }

  @Override
  public void toggle() {
    this.high = !high;
    fireStateChangeEvent();
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public void addPinStateChangeListener(PinStateChangeListener listener) {
    this.stateEventListeners.add(listener);
  }

  @Override
  public String toString() {
    return "SimulatorDigitalOutputPin on pin " + pin;
  }

  // --------------------------- Helper ----------------------------------------

  private void fireStateChangeEvent() {
    PinState state = PinState.HIGH;
    if(!high) {
      state = PinState.LOW;
    }
    for(PinStateChangeListener l : this.stateEventListeners) {
      l.pinStateChanged(new PinStateChangeEventImpl(this, pin, state) {
      });
    }
  }
}
