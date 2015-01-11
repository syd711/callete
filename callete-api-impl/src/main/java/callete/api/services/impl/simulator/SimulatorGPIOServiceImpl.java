package callete.api.services.impl.simulator;

import callete.api.services.gpio.*;

/**
 * A simple service for simulating GPIO.
 * <p/>
 * If you are developing a new application, you may not want to deploy the application
 * every time you want to test other features than GPIO. This is why this simulator
 * visualizes the GPIO states and provides basic input features.
 */
public class SimulatorGPIOServiceImpl {
  
  public ShiftRegister74hc595 connectShiftRegister(int chips, int serPin, int rclkPin, int srclkPin, String name) {
    return null;
  }

  public PushButton connectPushButton(int pin, String name) {
    SimulatorPushButton simulatorPushButton = new SimulatorPushButton(pin, name);
    Simulator.getInstance().addComponent(name, simulatorPushButton);
    return simulatorPushButton;
  }

  public DigitalOutputPin connectDigitalOutputPin(int pin, PinState pinState, String name) {
    SimulatorDigitalOutputPin simulatorDigitalOutputPin = new SimulatorDigitalOutputPin(pin, name, pinState);
    Simulator.getInstance().addComponent(name, simulatorDigitalOutputPin);
    return simulatorDigitalOutputPin;
  }

  public RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name) {
    SimulatorRotaryEncoder simulatorRotaryEncoder = new SimulatorRotaryEncoder(pinA, pinB, name);
    Simulator.getInstance().addComponent(name, simulatorRotaryEncoder);
    return simulatorRotaryEncoder;
  }

  public Object convertPinStateToApiInstance(PinState state) {
    return null;
  }


  public Object convertPinToApiInstance(int pin) {
    return null;
  }
}
