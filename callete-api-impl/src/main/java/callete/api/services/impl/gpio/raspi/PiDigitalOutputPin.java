package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import callete.api.services.gpio.DigitalOutputPin;
import callete.api.services.gpio.PinState;
import callete.api.services.gpio.PinStateChangeListener;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

import java.util.ArrayList;
import java.util.List;

/**
 * Raspberry Pi implementation of a digital output pin.
 */
public class PiDigitalOutputPin implements DigitalOutputPin {
  private int pin;
  private String name;
  private GpioPinDigitalOutput gpioPinDigitalOutput;
  private List<PinStateChangeListener> pinStateListeners = new ArrayList<>();

  public PiDigitalOutputPin(int pin, String name, PinState pinState) {
    this.pin = pin;
    this.name = name;
    Pin raspiPin = (Pin) Callete.getGPIOService().convertPinToApiInstance(pin);
    com.pi4j.io.gpio.PinState state = (com.pi4j.io.gpio.PinState) Callete.getGPIOService().convertPinStateToApiInstance(pinState);

    gpioPinDigitalOutput = GpioFactory.getInstance().provisionDigitalOutputPin(raspiPin, this.toString(), state);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void low() {
    gpioPinDigitalOutput.low();
    fireStateChangeEvent();
  }

  @Override
  public void high() {
    gpioPinDigitalOutput.high();
    fireStateChangeEvent();
  }

  @Override
  public void toggle() {
    gpioPinDigitalOutput.toggle();
    fireStateChangeEvent();
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public void addPinStateChangeListener(PinStateChangeListener listener) {
    pinStateListeners.add(listener);
  }

  @Override
  public String toString() {
    return "PiDigitalOutputPin on pin " + pin;
  }

  // --------------------------- Helper ----------------------------------------

  private void fireStateChangeEvent() {
    PinState state = PinState.HIGH;
    if (gpioPinDigitalOutput.isLow()) {
      state = PinState.LOW;
    }
    for (PinStateChangeListener l : this.pinStateListeners) {
      l.pinStateChanged(new PinStateChangeEventImpl(this, pin, state) {
      });
    }
  }
}
