package callete.api.services.impl.gpio.raspi;

import callete.api.services.gpio.PushEvent;

/**
 * Implements the Push Event interface for the Raspberry Pi GPIO API.
 */
public class PushEventImpl implements PushEvent {

  private Object source;
  private int pin;
  private boolean longPush;

  public PushEventImpl(Object source, int pin, boolean longPush) {
    this.source = source;
    this.pin = pin;
    this.longPush = longPush;
  }

  @Override
  public Object getSource() {
    return source;
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public boolean isLongPush() {
    return longPush;
  }
}
