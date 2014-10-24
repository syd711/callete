package callete.api.services.impl.simulator;

import callete.api.services.gpio.GPIOComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper that contains the simulator GPIO components.
 */
public class Simulator {

  private static Simulator instance;

  private Map<String, GPIOComponent> gpioComponents = new HashMap<>();

  public static Simulator getInstance() {
    if(instance == null) {
      instance = new Simulator();
    }
    return instance;
  }

  public GPIOComponent getGpioComponent(String name) {
    return gpioComponents.get(name);
  }

  public void addComponent(String name, GPIOComponent o) {
    this.gpioComponents.put(name, o);
  }
}
