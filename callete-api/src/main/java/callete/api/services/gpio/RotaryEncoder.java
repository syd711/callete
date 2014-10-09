package callete.api.services.gpio;

/**
 * Rotary encoder interface to be implemented by the concrete board implementation.
 */
public interface RotaryEncoder {

  /**
   * Add a listener to the rotary encoder that is fired when
   * the encoder is rotated left or right (not used for push buttons)
   * @param listener The listener to add to the rotary encoder.
   */
  void addChangeListener(RotaryEncoderListener listener);

  /**
   * Returns the name of the rotary encoder
   */
  String getName();
}
