package callete.api.services.impl.system;

import callete.api.services.system.SystemService;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Implements the system service interface.
 */
@SuppressWarnings("unused")
public class SystemServiceImpl implements SystemService {

  @Override
  public long getMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  @Override
  public long getTotalMemory() {
    return Runtime.getRuntime().totalMemory();
  }

  @Override
  public long getFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  @Override
  public long getAvailableDiskSpace() {
    return new File(".").getFreeSpace();
  }

  @Override
  public long getUsedDiskSpace() {
    final File file = new File(".");
    return file.getTotalSpace() - file.getFreeSpace();
  }

  @Override
  public String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return null;
    }
  }

  @Override
  public String getHostAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return null;
    }
  }
}
