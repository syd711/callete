package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import callete.api.services.gpio.DigitalOutputPin;
import callete.api.services.gpio.GPIOService;
import callete.api.services.gpio.PinState;
import callete.api.services.gpio.ShiftRegister74hc595;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The 8-bit shift register implementation using the pi GPIO API
 */
public class PiShiftRegister74hc595 implements ShiftRegister74hc595 {
  private final static Logger LOG = LoggerFactory.getLogger(PiShiftRegister74hc595.class);
  private String name;

  private int serPin;
  private int rclkPin;
  private int srclkPin;

  private PinState[] registerStates;

  private DigitalOutputPin ser;
  private DigitalOutputPin rclk;
  private DigitalOutputPin srclk;


  public PiShiftRegister74hc595(int chips, int serPin, int rclkPin, int srclkPin, String name) {
    this.name = name;
    this.serPin = serPin;
    this.rclkPin = rclkPin;
    this.srclkPin = srclkPin;

    int numOfRegisterPins = chips * 8;
    registerStates = new PinState[numOfRegisterPins];

    connect();
  }

  @Override
  public void setRegisterPin(int pin, PinState pinState) {
    try {
      this.registerStates[pin] = pinState;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      LOG.error("State for pin " + pin + " can not be changed, invalid index.");
    }
  }

  @Override
  public void clearRegisters() {
    for(int i = 0; i < registerStates.length; i++) {
      this.registerStates[i] = PinState.LOW;
    }
  }

  @Override
  public void writeRegisters() {
    rclk.low();

    for(int i = registerStates.length - 1; i >= 0; i--) {
      srclk.low();

      PinState registerState = registerStates[i];
      ser.setState(registerState);

      srclk.high();
    }

    rclk.high();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "ShiftRegister '" + name + "'";
  }

  //--------------------------- Helper---------------------------------------------------

  private void connect() {
    LOG.info("Connecting " + this);
    GPIOService gpioService = Callete.getGPIOService();
    LOG.info("Connecting SER pin");
    ser = gpioService.connectDigitalOutputPin(serPin, "SER", PinState.LOW);
    LOG.info("Connecting RCLK pin");
    rclk = gpioService.connectDigitalOutputPin(rclkPin, "RCLK", PinState.LOW);
    LOG.info("Connecting SRCLK pin");
    srclk = gpioService.connectDigitalOutputPin(srclkPin, "SRCLK", PinState.LOW);
  }
}
