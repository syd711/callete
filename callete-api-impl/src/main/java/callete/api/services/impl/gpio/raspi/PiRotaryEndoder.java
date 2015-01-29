package callete.api.services.impl.gpio.raspi;

import callete.api.Callete;
import callete.api.services.gpio.RotaryEncoder;
import callete.api.services.gpio.RotaryEncoderEvent;
import callete.api.services.gpio.RotaryEncoderListener;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rotary encoder implementation for the Raspberry Pi.
 */
public class PiRotaryEndoder implements RotaryEncoder, GpioPinListenerDigital {
  private final static Logger LOG = LoggerFactory.getLogger(PiRotaryEndoder.class);

  private int pinA;
  private int pinB;
  private String name;

  private List<RotaryEncoderListener> listeners = new ArrayList<>();
  private List<RotaryEncoderEvent> eventQueue = Collections.synchronizedList(new ArrayList<>());

  private GpioPinDigitalInput inputA;
  private GpioPinDigitalInput inputB;

  private long encoderValue = 0;
  private boolean firstPass = true;
  private boolean ignoreHalfSteps = true;
  private boolean toLeft = false;

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
  public void setIgnoreHalfSteps(boolean b) {
    this.ignoreHalfSteps = b;
  }

  @Override
  public void addChangeListener(RotaryEncoderListener listener) {
    listeners.add(listener);
  }

  /**
   * Creepy impl for a rotary encoder. A previous version of this file contains a correct
   * way to decode the encoder, but this way didn't work with my ALPS encoder, so whatever works...
   * @param event
   */
  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    //int stateA = inputA.getState().getValue();
    int stateB = inputB.getState().getValue();

    if(firstPass && ignoreHalfSteps) {
      toLeft = stateB == 0;
      if(toLeft) {
        encoderValue--;
      }
      else {
        encoderValue++;
      }
      firstPass = false;
    } else {
      //reset first pass flag
      firstPass = true;

      RotaryEncoderEventImpl e = new RotaryEncoderEventImpl(encoderValue, toLeft);
      addToEventQueue(e);
      emitEvent();
    }
  }

  // --------------------- Helper -----------------------------------

  /**
   * Removes an event from the event queue and fires it to all listeners.
   * The event is removed from the queue afterwards.
   */
  private void emitEvent() {
    new Thread() {
      @Override
      public void run() {
        synchronized(eventQueue) {
          if(!eventQueue.isEmpty()) {
            RotaryEncoderEvent e = eventQueue.get(0);
            LOG.debug(this + " fires event: toLeft=" + e.rotatedLeft() + ", steps=" + e.getSteps());
            for(RotaryEncoderListener l : listeners) {
              l.rotated(e);
            }
            eventQueue.clear();
          }
        }
      }
    }.start();
  }

  /**
   * Adds the event to the event queue.
   * If there is already an event that has not been emitted yet, the given event is ignored.
   */
  private void addToEventQueue(RotaryEncoderEvent e) {
    synchronized(eventQueue) {
      if(eventQueue.isEmpty()) {
        eventQueue.add(e);
      }
    }
  }

  /**
   * Creates the digital input pins and their listeners.
   */
  private void registerInputListeners() {
    Pin raspiPinA = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinA);
    Pin raspiPinB = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinB);

    inputA = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinA, PinPullResistance.PULL_UP);
    inputB = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinB, PinPullResistance.PULL_UP);

    inputA.addListener(this);
  }


  @Override
  public String toString() {
    return "PiRotaryEncoder '" + name + "' on pin " + pinA + " and pin " + pinB;
  }
}
