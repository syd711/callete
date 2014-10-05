package callete.api.services.gpio;

/**
 * Event listener fired when the rotary encoder value has changed.
 */
public interface RotaryEncoderListener {

  /**
   * The event fired when the encoder has been rotated.
   */
  void rotated(RotaryEncoderEvent event);
}
