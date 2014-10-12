package callete.samples;

import callete.api.Callete;
import callete.api.services.gpio.*;

import java.io.IOException;

/**
 * Sample to turn a LED on and off with a push button.
 */
public class PushButtonExample {

  public static void main(String[] args) throws InterruptedException, IOException {
    GPIOService gpioService = Callete.getGPIOService();
    final DigitalOutputPin ledPin = gpioService.connectDigitalOutputPin(12, PinState.HIGH);
    PushButton pushButton = gpioService.connectPushButton(13, "Toggle Button");
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

      @Override
      public long getLongPushDebounceMillis() {
        return 1000;
      }
    });

    System.in.read();
  }
}
