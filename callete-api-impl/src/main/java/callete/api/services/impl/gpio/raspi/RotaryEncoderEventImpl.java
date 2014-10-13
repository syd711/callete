package callete.api.services.impl.gpio.raspi;

import callete.api.services.gpio.RotaryEncoderEvent;

/**
 * The event implementation for rotary encoder changes.
 */
public class RotaryEncoderEventImpl implements RotaryEncoderEvent {
  private long steps;
  private boolean left;

  public RotaryEncoderEventImpl(long steps, boolean left) {
    this.steps = steps;
    this.left = left;
  }

  @Override
  public long getSteps() {
    return steps;
  }

  @Override
  public boolean rotatedLeft() {
    return left;
  }
}
