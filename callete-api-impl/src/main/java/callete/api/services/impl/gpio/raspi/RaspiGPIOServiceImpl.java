package callete.api.services.impl.gpio.raspi;

import com.pi4j.io.gpio.RaspiPin;
import callete.api.services.gpio.*;
import callete.api.services.impl.gpio.GPIOServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Raspberry Pi GPIO implementation, using pi4j.
 */
public class RaspiGPIOServiceImpl {
  private final static Logger LOG = LoggerFactory.getLogger(GPIOServiceImpl.class);

  public PushButton connectPushButton(int pin, String name) {
    LOG.info("Creating push button for pin " + pin);
    return new PiPushButton(pin, name);
  }

  public RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name) {
    LOG.info("Creating rotary encoder for pin " + pinA + " and pin " + pinB);
    return new PiRotaryEndoder(pinA, pinB, name);
  }

  public DigitalOutputPin connectDigitalOutputPin(int pin, PinState pinState) {
    LOG.info("Creating digital output pin " + pin);
    return new PiDigitalOutputPin(pin, pinState);
  }

  public Object convertPinStateToApiInstance(PinState state) {
    if(state.equals(PinState.HIGH)) {
      return com.pi4j.io.gpio.PinState.HIGH;
    }
    return com.pi4j.io.gpio.PinState.LOW;
  }

  public Object convertPinToApiInstance(int pin) {
    switch (pin) {
      case 3:
        return RaspiPin.GPIO_08;
      case 5:
        return RaspiPin.GPIO_09;
      case 7:
        return RaspiPin.GPIO_07;
      case 8:
        return RaspiPin.GPIO_15;
      case 10:
        return RaspiPin.GPIO_16;
      case 11:
        return RaspiPin.GPIO_00;
      case 12:
        return RaspiPin.GPIO_01;
      case 13:
        return RaspiPin.GPIO_02;
      case 15:
        return RaspiPin.GPIO_03;
      case 16:
        return RaspiPin.GPIO_04;
      case 18:
        return RaspiPin.GPIO_05;
      case 19:
        return RaspiPin.GPIO_12;
      case 21:
        return RaspiPin.GPIO_13;
      case 22:
        return RaspiPin.GPIO_06;
      case 23:
        return RaspiPin.GPIO_14;
      case 24:
        return RaspiPin.GPIO_10;
      case 26:
        return RaspiPin.GPIO_11;
      default:
        throw new GPIOException("Error mapping numeric pin " + pin + " to concrete API instance.");
    }
  }
}
