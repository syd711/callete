package callete.api.services.impl.simulator;

import callete.api.services.gpio.PushButton;
import callete.api.services.gpio.PushListener;
import callete.api.services.impl.gpio.raspi.PushEventImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulator Push button will be represented by a button widget in the simulator.
 */
public class SimulatorPushButton implements PushButton {
  private List<PushListener> listeners = new ArrayList<>();
  private String name;
  private int pin;

  public SimulatorPushButton(int pin, String name) {
    this.name = name;
    this.pin = pin;
  }

  @Override
  public void addPushListener(PushListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public String getName() {
    return name;
  }

  public void push(boolean longPush) {
    for(PushListener l : listeners) {
      l.pushed(new PushEventImpl(this, pin, longPush));
    }
  }
}
