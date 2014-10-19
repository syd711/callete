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
  public String getUsedDiskSpace() {
    long used = new File(".").getTotalSpace()-new File(".").getFreeSpace();
    return String.valueOf(used/1024/1024);
  }

  @Override
  public String getAvailableDiskSpace() {
    return String.valueOf(new File(".").getFreeSpace()/1024/1024);
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
