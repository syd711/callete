package callete.api.services.impl.gpio.raspi;

import callete.api.services.gpio.RotaryEncoderEvent;

/**
 * The event implementation for rotary encoder changes.
 */
public class RotaryEncoderEventImpl implements RotaryEncoderEvent {
  private int steps;
  private boolean left;

  public RotaryEncoderEventImpl(int steps, boolean left) {
    this.steps = steps;
    this.left = left;
  }

  @Override
  public int getSteps() {
    return steps;
  }

  @Override
  public boolean rotatedLeft() {
    return left;
  }
}
