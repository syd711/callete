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
  private ENCODING_MODE mode;


  private int lastEncoded = 0;
  private static final int stateTable[][] = {
      {0, 1, 1, -1},
      {-1, 0, 1, -1},
      {-1, 1, 0, -1},
      {-1, 1, 1, 0}
  };

  private Thread emitThread;
  private boolean emitThreadRunning = true;

  public PiRotaryEndoder(int pinA, int pinB, String name, PiRotaryEndoder.ENCODING_MODE mode) {
    this.pinA = pinA;
    this.pinB = pinB;
    this.name = name;
    this.mode = mode;

    registerInputListeners();
    this.initEmitterThread();
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
   *
   * @param event
   */
  @Override
  public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
    if (mode.equals(ENCODING_MODE.STATE_TABLE)) {
      stateEncoding(event);
    }
    else {
      manualEncoding(event);
    }
  }

  private void stateEncoding(GpioPinDigitalStateChangeEvent event) {
    int stateA = inputA.getState().getValue();
    int stateB = inputB.getState().getValue();

    // converting the 2 pin value to single number to end up with 00, 01, 10 or 11
    int encoded = (stateA << 1) | stateB;

    if (firstPass) {
      firstPass = false;
    }
    else {
      // going up states, 01, 11
      // going down states 00, 10
      int state = stateTable[lastEncoded][encoded];
      encoderValue += state;

      //ignore half steps, so we use %2
      if (ignoreHalfSteps && ((encoderValue % 2) != 0)) {
        return;
      }

      RotaryEncoderEventImpl e = new RotaryEncoderEventImpl(encoderValue, state == -1);
    }
  }

  private void manualEncoding(GpioPinDigitalStateChangeEvent event) {
    int stateA = inputA.getState().getValue();
    int stateB = inputB.getState().getValue();
//    LOG.debug("[" + event.getPin().getName() + "] Edge " + event.getEdge().getName() + "=" + event.getEdge().getValue() + "; State " + event.getState().getName() + "="  + event.getState().getValue() + " States: " + stateA + "/" +stateB);
    if (firstPass && ignoreHalfSteps) {
      toLeft = stateB == 0;
      if (toLeft) {
        encoderValue--;
      }
      else {
        encoderValue++;
      }
      firstPass = false;
    }
    else {
      //reset first pass flag
      firstPass = true;

      RotaryEncoderEventImpl e = new RotaryEncoderEventImpl(encoderValue, toLeft);
      addToEventQueue(e);
    }
  }

  private void initEmitterThread() {
    this.emitThread = new Thread() {
      @Override
      public void run() {
        try {
          while (emitThreadRunning) {
            if (!eventQueue.isEmpty()) {
              RotaryEncoderEvent e = eventQueue.get(0);
              LOG.debug(this + " fires event: toLeft=" + e.rotatedLeft() + ", steps=" + e.getSteps());
              for (RotaryEncoderListener l : listeners) {
                l.rotated(e);
              }
              Thread.sleep(250);
              eventQueue.clear();
            }

            synchronized (this) {
              this.wait();
            }
          }
        } catch (InterruptedException e) {
          LOG.error("Failed to emit event queue", e);
        }
      }
    };
    this.emitThread.start();
  }

  // --------------------- Helper -----------------------------------

  /**
   * Adds the event to the event queue.
   * If there is already an event that has not been emitted yet, the given event is ignored.
   */
  private void addToEventQueue(RotaryEncoderEvent e) {
    eventQueue.add(e);
    synchronized (emitThread) {
      this.emitThread.notifyAll();
    }
  }

  /**
   * Creates the digital input pins and their listeners.
   */
  private void registerInputListeners() {
    Pin raspiPinA = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinA);
    LOG.info("Register rotary encoder input listener for " + raspiPinA);
    Pin raspiPinB = (Pin) Callete.getGPIOService().convertPinToApiInstance(pinB);
    LOG.info("Register rotary encoder input listener for " + raspiPinB);

    inputA = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinA, PinPullResistance.PULL_UP);
    inputB = GpioFactory.getInstance().provisionDigitalInputPin(raspiPinB, PinPullResistance.PULL_UP);

    inputA.addListener(this);
    inputB.addListener(this);
  }


  @Override
  public String toString() {
    return "PiRotaryEncoder '" + name + "' on pin " + pinA + " and pin " + pinB;
  }
}
