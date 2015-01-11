package callete.api.services.impl.gpio;

import callete.api.services.gpio.*;
import callete.api.services.impl.gpio.raspi.RaspiGPIOServiceImpl;
import callete.api.services.impl.simulator.Simulator;
import callete.api.services.impl.simulator.SimulatorGPIOServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GPIO service implementation for the Raspberry Pi.
 * //TODO the simulation stuff is just sloppy but works so far 
 */
@SuppressWarnings("unused")
public class GPIOServiceImpl implements GPIOService {
  private final static Logger LOG = LoggerFactory.getLogger(GPIOServiceImpl.class);

  private RaspiGPIOServiceImpl raspiGPIO = new RaspiGPIOServiceImpl();
  private SimulatorGPIOServiceImpl simGPIO;
  private boolean simulationMode = false;

  @Override
  public ShiftRegister74hc595 connectShiftRegister(int chips, int serPin, int rclkPin, int srclkPin, String name) {
    ShiftRegister74hc595 register = getSimulator().connectShiftRegister(chips, serPin, rclkPin, srclkPin, name);
    if(simulationMode) {
      return register;
    }
    return raspiGPIO.connectShiftRegister(chips, serPin, rclkPin, srclkPin, name);
  }

  @Override
  public PushButton connectPushButton(int pin, String name) {
    PushButton simulatedPushButton = getSimulator().connectPushButton(pin, name);
    if(simulationMode) {
      return simulatedPushButton;
    }
    return raspiGPIO.connectPushButton(pin, name);
  }

  @Override
  public RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name) {
    RotaryEncoder simulatedRotaryEncoder = getSimulator().connectRotaryEncoder(pinA, pinB, name);
    if(simulationMode) {
      return simulatedRotaryEncoder;
    }
    return raspiGPIO.connectRotaryEncoder(pinA, pinB, name);
  }

  @Override
  public DigitalOutputPin connectDigitalOutputPin(int pin, String name, PinState pinState) {
    DigitalOutputPin simulatedDigitalOutputPin = getSimulator().connectDigitalOutputPin(pin, pinState, name);
    if(simulationMode) {
      return simulatedDigitalOutputPin;
    }
    return raspiGPIO.connectDigitalOutputPin(pin, name, pinState);
  }

  @Override
  public Object convertPinStateToApiInstance(PinState state) {
    if(simulationMode) {
      return null;
    }
    return raspiGPIO.convertPinStateToApiInstance(state);
  }

  @Override
  public Object convertPinToApiInstance(int pin) {
    if(simulationMode) {
      return null;
    }
    return raspiGPIO.convertPinToApiInstance(pin);
  }

  @Override
  public GPIOComponent getSimulatedGPIOComponent(String name) {
    return Simulator.getInstance().getGpioComponent(name);
  }

  @Override
  public void setSimulationMode(boolean b) {
    this.simulationMode = b;
  }

  // ------------------------------ Helper --------------------------------

  /**
   * Lazy creation of the GPIO simulator.
   */
  private SimulatorGPIOServiceImpl getSimulator() {
    if(simGPIO == null) {
      LOG.info("Creating GPIO simulator");
      simGPIO = new SimulatorGPIOServiceImpl();
    }
    return simGPIO;
  }
}
