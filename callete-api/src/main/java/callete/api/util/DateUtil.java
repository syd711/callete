package callete.api.util;

/**
 * Utility class for formatting dates, etc.
 */
public class DateUtil {

  public static String formatTime(int secs) {
    String minutes = String.valueOf(secs / 60);
    String seconds = String.valueOf(secs % 60);
    if(secs < 10) {
      seconds = "0" + seconds;
    }
    if(secs % 60 == 0) {
      seconds = "00";
    }
    return minutes + ":" + seconds;
  }
}
