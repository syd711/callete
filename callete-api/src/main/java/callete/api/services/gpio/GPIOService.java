package callete.api.services.gpio;

import callete.api.services.Service;

/**
 * The service handles all GPIO activities and must be implemented
 * for the concrete developer board, e.g. raspberry pi or the beaglebone black.
 */
public interface GPIOService extends Service {

  /**
   * Registers a push button to the GPIO service.
   * Additional settings are configured on the button itself.
   * The pin depends on the flavor and must not necessarily the number of a GPIO pin.
   *
   * E.g. for the Raspberry Pi the actual pin "12" would be GPIO pin "1".
   * Since the API does not only deal with GPIO ports it's recommended to use the real pin id and not
   * the GPIO number here.
   *
   * @param pin The pin the button is connected to.
   * @param name The name of the button, used during emulation.
   * @return A push button instance where different event listeners can be registered.
   */
  PushButton connectPushButton(int pin, String name);

  /**
   * Registers a rotary encoder to the GPIO service.
   * Additional settings are configured on the rotary encoder itself.
   * A pin number has to be provided for both directions.
   * If the rotary encoder provides a push button, this one has to be
   * registered using the #connectPushButton method.
   *
   * E.g. for the Raspberry Pi the actual pin "12" would be GPIO pin "1".
   * Since the API does not only deal with GPIO ports it's recommended to use the real pin id and not
   * the GPIO number here.
   *
   * @param pinA The pin A of the rotary encoder
   * @param pinB The pin B of the rotary encoder
   * @param name The name of the rotary encoder, used during emulation.
   * @return A push button instance where different event listeners can be registered.
   */
  RotaryEncoder connectRotaryEncoder(int pinA, int pinB, String name);

  /**
   * Concrete API implementation to map the numeric pin number to the API instance.
   *
   * E.g. for the Raspberry Pi the actual pin number 12 would be mapped to the RaspiPin.Pin instance with id "1".
   * @param pin The numeric pin id that should be mapped.
   * @return The concrete API representation.
   */
  Object convertPinToApiInstance(int pin);

  /**
   * Concrete API implementation to map the pin state to the API instance.
   *
   * @param state The pin state that should be mapped.
   * @return The concrete API representation.
   */
  Object convertPinStateToApiInstance(PinState state);

  /**
   * Connect a GPIO pin with the initial given state.
   * @param pin the numeric pin id of the board to use, will be converted by the #convertPinToApiInstance method.
   * @param name the name the pin is identified with
   * @param pinState the initial state of the pin
   * @return A pin representation
   */
  DigitalOutputPin connectDigitalOutputPin(int pin, String name, PinState pinState);

  /**
   * Disabled by default.
   *
   * If enabled, simulator components will be build and stored in the Simulator singleton.
   * @param enabled If true, the simulator will be used instead of real GPIO.
   */
  void setSimulationMode(boolean enabled);
}