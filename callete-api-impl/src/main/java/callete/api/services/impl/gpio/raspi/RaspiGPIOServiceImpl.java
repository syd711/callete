package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import callete.api.services.gpio.*;
import callete.api.services.impl.gpio.GPIOServiceImpl;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Raspberry Pi GPIO implementation, using pi4j.
 */
public class RaspiGPIOServiceImpl {
  private final static Logger LOG = LoggerFactory.getLogger(GPIOServiceImpl.class);
  
  private final static String RASPBERRY_PI_MODEL_B = "Raspberry Pi Model B";
  private final static String RASPBERRY_PI_2_MODEL_B = "Raspberry Pi 2 Model B";
  
  private String board;

  public PiShiftRegister74hc595 connectShiftRegister(int chips, int serPin, int rclkPin, int srclkPin, String name) {
    LOG.info("Creating shift register '" + name + "' using pins " + serPin + ", " + rclkPin + ", "+  srclkPin);
    return new PiShiftRegister74hc595(chips, serPin, rclkPin, srclkPin, name);
  }
  
  public PushButton connectPushButton(int pin, String name) {
    LOG.info("Creating push button for pin " + pin);
    return new PiPushButton(pin, name);
  }

  public RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name) {
    LOG.info("Creating rotary encoder for pin " + pinA + " and pin " + pinB);
    return new PiRotaryEndoder(pinA, pinB, name);
  }

  public DigitalOutputPin connectDigitalOutputPin(int pin, String name, PinState pinState) {
    LOG.info("Creating digital output pin " + pin);
    return new PiDigitalOutputPin(pin, name, pinState);
  }

  public Object convertPinStateToApiInstance(PinState state) {
    if(state.equals(PinState.HIGH)) {
      return com.pi4j.io.gpio.PinState.HIGH;
    }
    return com.pi4j.io.gpio.PinState.LOW;
  }

  public Object convertPinToApiInstance(int pin) {
    if(board == null) {
      board = Callete.getConfiguration().getString("gpio.board");
      if(board == null) {
        LOG.warn("No 'gpio.board' configuration found, applying pin of " + RASPBERRY_PI_MODEL_B + " as default.");
        board = RASPBERRY_PI_MODEL_B;
      }
      
      if(board.equals(RASPBERRY_PI_MODEL_B)) {
        LOG.info("Using GPIO configuration for board " + RASPBERRY_PI_MODEL_B);
      }
      else if(board.equals(RASPBERRY_PI_2_MODEL_B)) {
        LOG.info("Using GPIO configuration for board " + RASPBERRY_PI_2_MODEL_B);
      }
      else {
        LOG.error("No valid board definition '" + board + "', appyling " + RASPBERRY_PI_MODEL_B + " instead.");
        board = RASPBERRY_PI_MODEL_B;
      }
    }

    if(board.equals(RASPBERRY_PI_MODEL_B)) {
      return convert2RaspberryPiModelBPin(pin);
    }
    else if(board.equals(RASPBERRY_PI_2_MODEL_B)) {
      return convert2RaspberryPi2ModelBPin(pin);
    }
    return convert2RaspberryPiModelBPin(pin);
  }

  private Object convert2RaspberryPi2ModelBPin(int pin) {
    switch(pin) {
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
      case 29:
        return RaspiPin.GPIO_21;
      case 31:
        return RaspiPin.GPIO_22;
      case 32:
        return RaspiPin.GPIO_26;
      case 33:
        return RaspiPin.GPIO_23;
      case 35:
        return RaspiPin.GPIO_24;
      case 36:
        return RaspiPin.GPIO_27;
      case 37:
        return RaspiPin.GPIO_25;
      case 38:
        return RaspiPin.GPIO_28;
      case 40:
        return RaspiPin.GPIO_29;
      default:
        throw new GPIOException("Error mapping numeric pin " + pin + " to concrete API instance.");
    }
  }

  private Object convert2RaspberryPiModelBPin(int pin) {
    switch(pin) {
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
