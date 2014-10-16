package callete.api.services.impl.simulator;

import callete.api.services.gpio.RotaryEncoder;
import callete.api.services.gpio.RotaryEncoderListener;
import callete.api.services.impl.gpio.raspi.RotaryEncoderEventImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulator Push button will be represented by a button widget in the simulator.
 */
public class SimulatorRotaryEncoder implements RotaryEncoder {
  private List<RotaryEncoderListener> listeners = new ArrayList<>();
  private String name;
  private int step = 0;

  public SimulatorRotaryEncoder(int pinA, int pinB, String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public void left() {
    step--;
    for(RotaryEncoderListener l : listeners) {
      l.rotated(new RotaryEncoderEventImpl(step, true));
    }
  }

  @Override
  public void setIgnoreHalfSteps(boolean b) {
    //not used here
  }

  public void right() {
    step++;
    for(RotaryEncoderListener l : listeners) {
      l.rotated(new RotaryEncoderEventImpl(step, false));
    }
  }

  @Override
  public void addChangeListener(RotaryEncoderListener listener) {
    this.listeners.add(listener);
  }
}
