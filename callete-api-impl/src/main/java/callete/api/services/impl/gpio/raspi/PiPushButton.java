package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import callete.api.services.gpio.PushButton;
import callete.api.services.gpio.PushListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Raspberry Pi implementation for the PushButton, using pi4j.
 */
public class PiPushButton implements PushButton {
  private final static Logger LOG = LoggerFactory.getLogger(PiPushButton.class);

  private GpioPinDigitalInput input;

  private List<PushListener> pushListeners = new ArrayList<>();

  private int pin;
  private String name;
  private long pushStart;

  public PiPushButton(int pin, String name) {
    this.pin = pin;
    this.name = name;

    Pin raspiPin = (Pin) Callete.getGPIOService().convertPinToApiInstance(pin);
    this.input = GpioFactory.getInstance().provisionDigitalInputPin(raspiPin, PinPullResistance.PULL_DOWN);

    registerGPIOListener();
  }

  @Override
  public int getPin() {
    return pin;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addPushListener(PushListener listener) {
    this.pushListeners.add(listener);
  }

  // ------------------------- Helper --------------------------------

  private void registerGPIOListener() {
    input.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        if (input.getPin().getAddress() == event.getPin().getPin().getAddress()) {
          PinState state = event.getState();
          if (state == PinState.LOW) {
            LOG.info("Push event detected for " + PiPushButton.this);
            long pushEnd = new Date().getTime();
            for(PushListener listener : pushListeners) {
              if (pushStart > 0 && (pushEnd - pushStart) > listener.getPushDebounceMillis()) {
                listener.pushed(new PushEventImpl(this, pin));
              }
            }
          }
          else {
            pushStart = new Date().getTime();
          }
        }
      }
    });
  }

  @Override
  public String toString() {
    return "PiPushButton on pin " + pin;
  }
}
