package callete.api.services.gpio;

/**
 * The event fired when the rotary encoder has changed.
 */
public interface RotaryEncoderEvent {

  /**
   * Returns the amount of steps the rotary encoder has been rotated.
   */
  long getSteps();

  /**
   * If true, the encoder has been rotated to the left, to right otherwise.
   */
  boolean rotatedLeft();
}
