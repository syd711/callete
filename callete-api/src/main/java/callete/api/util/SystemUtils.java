package callete.api.util;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Utility method for system depending operations.
 */
public class SystemUtils {
  private final static Logger LOG = LoggerFactory.getLogger(SystemUtils.class);

  public final static String DELIMITER = System.getProperty("line.separator");

  /**
   * Returns true if VM is running on windows.
   */
  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  public static boolean isWindows(String osName) {
    return osName.toLowerCase().contains("windows");
  }

  public static String resolveHostAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      LOG.error("Unable to resolve host address: " + e.getMessage(), e);
    }
    return null;
  }

  public static String humanReadableByteCount(long bytes) {
    int unit = 1024;
    if(bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = ("kMGTPE").charAt(exp - 1) + ("");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }

  public static boolean executeSystemCommand(String dir, List<String> cmds) {
    LOG.info("Executing system command: " + Joiner.on(" ").join(cmds));
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(cmds).inheritIO().redirectErrorStream(true);

      //determine the directory the maven call should be executed.
      File deploymentDirectory = new File(dir);
      processBuilder.directory(deploymentDirectory);

      //execute the process build
      Process p = processBuilder.start();
      return p.waitFor() == 0;
    } catch (Exception e) {
      LOG.error("Failed to system command for creating directory: " + e.getMessage(), e);
    }
    return false;
  }

  public static String execute(List<String> cmds) {
    try {
      LOG.info("Executing system command: " + Joiner.on(" ").join(cmds));
      SystemCommandExecutor exec = new SystemCommandExecutor(cmds);
      exec.executeCommand();

      String error = exec.getStandardErrorFromCommand().toString();
      if(error != null && error.trim().length() > 0) {
        LOG.error("Error executing command: " + error);
      }
      return exec.getStandardOutputFromCommand().toString();
    } catch (Exception e) {
      LOG.error("Failed to execute command: " + e.getMessage(), e);
    }
    return null;
  }
}
