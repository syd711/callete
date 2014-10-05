package callete.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility method for system depending operations.
 */
public class SystemUtils {
  private final static Logger LOG = LoggerFactory.getLogger(SystemUtils.class);

  /**
   * Returns true if VM is running on windows.
   */
  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  @Nullable
  public static String resolveHostAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      LOG.error("Unable to resolve host address: " + e.getMessage(), e);
    }
    return null;
  }
}
