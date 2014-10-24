package callete.api.services.impl.gpio;

import callete.api.services.gpio.*;
import callete.api.services.impl.gpio.raspi.RaspiGPIOServiceImpl;
import callete.api.services.impl.simulator.SimulatorGPIOServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GPIO service implementation for the Raspberry Pi.
 */
@SuppressWarnings("unused")
public class GPIOServiceImpl implements GPIOService {
  private final static Logger LOG = LoggerFactory.getLogger(GPIOServiceImpl.class);

  private RaspiGPIOServiceImpl raspiGPIO = new RaspiGPIOServiceImpl();
  private SimulatorGPIOServiceImpl simGPIO;
  private boolean simulationMode = false;

  @Override
  public PushButton connectPushButton(int pin, String name) {
    if (simulationMode) {
      return getSimulator().connectPushButton(pin, name);
    }
    return raspiGPIO.connectPushButton(pin, name);
  }

  @Override
  public RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name) {
    if (simulationMode) {
      return getSimulator().connectRotaryEncoder(pinA, pinB, name);
    }
    return raspiGPIO.connectRotaryEncoder(pinA, pinB, name);
  }

  @Override
  public DigitalOutputPin connectDigitalOutputPin(int pin, String name, PinState pinState) {
    if (simulationMode) {
      return getSimulator().connectDigitalOutputPin(pin, pinState, name);
    }
    return raspiGPIO.connectDigitalOutputPin(pin, name, pinState);
  }

  @Override
  public Object convertPinStateToApiInstance(PinState state) {
    if (simulationMode) {
      return null;
    }
    return raspiGPIO.convertPinStateToApiInstance(state);
  }

  @Override
  public Object convertPinToApiInstance(int pin) {
    if (simulationMode) {
      return null;
    }
    return raspiGPIO.convertPinToApiInstance(pin);
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
    if (simGPIO == null) {
      LOG.info("Creating GPIO simulator");
      simGPIO = new SimulatorGPIOServiceImpl();
    }
    return simGPIO;
  }
}
