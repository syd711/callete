package callete.samples;

import callete.api.Callete;
import callete.api.services.gpio.*;
import callete.api.util.SystemUtils;

import java.io.IOException;

/**
 * Sample to turn a LED on and off with a push button.
 */
public class GPIOSimulationExample {

  public static void main(String[] args) throws InterruptedException, IOException {
    GPIOService gpioService = Callete.getGPIOService();
    gpioService.setSimulationMode(SystemUtils.isWindows());
    final DigitalOutputPin ledPin = gpioService.connectDigitalOutputPin(12, PinState.HIGH);
    PushButton pushButton = gpioService.connectPushButton(13, "Push Button !");
    pushButton.addPushListener(new PushListener() {
      @Override
      public void pushed(PushEvent pushEvent) {
        System.out.println("Pushed detected, toggling LED!");
        ledPin.toggle();
      }

      @Override
      public long getPushDebounceMillis() {
        return 700;
      }
    });

    System.in.read();
  }
}
