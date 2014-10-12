package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import callete.api.services.gpio.RotaryEncoder;
import callete.api.services.gpio.RotaryEncoderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Rotary encoder implementation for the Raspberry Pi.
 */
public class PiRotaryEndoder implements RotaryEncoder, GpioPinListenerDigital {

  private int pinA;
  private int pinB;
  private String name;

  private List<RotaryEncoderListener> listeners = new ArrayList<>();

  private GpioPinDigitalInput inputA;
  private GpioPinDigitalInput inputB;

  private int lastEncoded;
  private int encoderValue;

  private static final int stateTable[][] = {
          {0, 1, 1, -1},
          {-1, 0, 1, -1},
          {-1, 1, 0, -1},
          {-1, 1, 1, 0}
  };

  public PiRotaryEndoder(int pinA, int pinB, String name) {
    this.pinA = pinA;
    this.pinB = pinB;
    this.name = name;

    registerInputListeners();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addChangeListener(RotaryEncoderListener listener) {
    listeners.add(listener);
  }

  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    int stateA = inputA.getState().getValue();
    int stateB = inputB.getState().getValue();

    // converting the 2 pin value to single number to end up with 00, 01, 10 or 11
    int encoded = (stateA << 1) | stateB;

    encoderValue += stateTable[lastEncoded][encoded];
    lastEncoded = encoded;

    System.out.println("Encoder Value: " + encoderValue);
    System.out.println("Last Encoded Value: " + lastEncoded);

    RotaryEncoderEventImpl e = new RotaryEncoderEventImpl(encoderValue, true);
    for (RotaryEncoderListener l : listeners) {
      l.rotated(e);
    }
  }

  // --------------------- Helper -----------------------------------

  /**
   * Creates the digital input pins and their listeners.
   */
  private void registerInputListeners() {
    Pin raspiPinA = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinA);
    Pin raspiPinB = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinB);

    inputA = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinA, PinPullResistance.PULL_DOWN);
    inputB = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinB, PinPullResistance.PULL_DOWN);

    inputA.addListener(this);
    inputB.addListener(this);
  }


  @Override
  public String toString() {
    return "PiRotaryEncoder '" + name + "' on pin " + pinA + " and pin " + pinB;
  }
}
