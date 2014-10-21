package callete.samples;

import callete.api.Callete;
import callete.api.services.gpio.GPIOService;
import callete.api.services.gpio.RotaryEncoder;
import callete.api.services.gpio.RotaryEncoderEvent;
import callete.api.services.gpio.RotaryEncoderListener;

import java.io.IOException;

/**
 * Sample to show how a rotary encoder works.
 */
public class RotaryEncoderExample {

  public static void main(String[] args) throws InterruptedException, IOException {
    GPIOService gpioService = Callete.getGPIOService();
    RotaryEncoder rotary = gpioService.connectRotaryEncoder(12, 16, "rotary");
    rotary.addChangeListener(new RotaryEncoderListener() {
      @Override
      public void rotated(RotaryEncoderEvent event) {
        System.out.println("Rotary steps: " + event.getSteps());
      }
    });

    System.in.read();
  }
}
