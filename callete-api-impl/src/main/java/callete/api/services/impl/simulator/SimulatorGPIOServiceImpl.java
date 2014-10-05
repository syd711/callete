package callete.api.services.impl.simulator;

import callete.api.services.gpio.DigitalOutputPin;
import callete.api.services.gpio.PinState;
import callete.api.services.gpio.PushButton;
import callete.api.services.impl.gpio.GPIOServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple service for simulating GPIO.
 *
 * If you are developing a new application, you may not want to deploy the application
 * every time you want to test other features than GPIO. This is why this simulator
 * visualizes the GPIO states and provides basic input features.
 */
public class SimulatorGPIOServiceImpl {
  private final static Logger LOG = LoggerFactory.getLogger(GPIOServiceImpl.class);

  public SimulatorGPIOServiceImpl() {
    Simulator.getInstance().show();
  }

  public PushButton connectPushButton(int pin, String name) {
    SimulatorPushButton simulatorPushButton = new SimulatorPushButton(pin, name);
    Simulator.getInstance().addPushButton(simulatorPushButton);
    return simulatorPushButton;
  }

  public DigitalOutputPin connectDigitalOutputPin(int pin, PinState pinState) {
    SimulatorDigitalOutputPin simulatorDigitalOutputPin = new SimulatorDigitalOutputPin(pin, pinState);
    Simulator.getInstance().addPin(simulatorDigitalOutputPin, pinState.equals(PinState.HIGH));
    return simulatorDigitalOutputPin;
  }

  public Object convertPinStateToApiInstance(PinState state) {
    return null;
  }

  public Object convertPinToApiInstance(int pin) {
    return null;
  }


}
