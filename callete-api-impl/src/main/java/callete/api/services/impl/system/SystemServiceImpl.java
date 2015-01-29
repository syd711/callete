package callete.api.services.impl.system;

import callete.api.Callete;
import callete.api.services.system.SystemService;
import callete.api.util.SystemCommandExecutor;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Implements the system service interface.
 */
@SuppressWarnings("unused")
public class SystemServiceImpl implements SystemService {
  private final static Logger LOG = LoggerFactory.getLogger(SystemServiceImpl.class);

  private MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

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
  public double getCpuUsage() {
    try {
      ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
      AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

      if(list.isEmpty()) return Double.NaN;

      Attribute att = (Attribute) list.get(0);
      Double value = (Double) att.getValue();

      if(value == -1.0) {
        return Double.NaN;  // usually takes a couple of seconds before we get real values
      }
      return ((int) (value * 1000) / 10.0);        // returns a percentage value with 1 decimal point precision
    } catch (Exception e) {
      LOG.error("Failed to determine CPU usage: " + e.getMessage(), e);
    }
    return Double.NaN;
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
    } catch (Exception e) {
      LOG.error("Error resolving host name: " + e.getMessage(), e);
      return null;
    }
  }

  @Override
  public String getHostAddress() {
    try {
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while(e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        Enumeration ee = n.getInetAddresses();
        while(ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();
          String address = i.getHostAddress();
          if(address.startsWith("192.")) {
            return address;
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Error resolving IP address: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public void reboot() {
    try {
      LOG.info("Executing rebooting command");
      SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo reboot"));
      executor.executeCommand();
      
      LOG.info(executor.getStandardOutputFromCommand().toString());
    } catch (Exception e) {
      LOG.error("Error executing reboot command: " + e.getMessage(), e);
    }
  }
  
  @Override
  public void deleteLogs() {
    try {
      LOG.info("Deleting logs");
      SystemCommandExecutor executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo rm /var/log/*.log"));
      executor.executeCommand();

      executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo rm /var/log/*.gz"));
      executor.executeCommand();
      
      executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo rm /var/log/messages"));
      executor.executeCommand();

      executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo rm /var/log/lastlog"));
      executor.executeCommand();

      executor = new SystemCommandExecutor(Splitter.on(" ").splitToList("sudo rm /var/log/syslog"));
      executor.executeCommand();
    } catch (Exception e) {
      LOG.error("Error deleting logs: " + e.getMessage(), e);
    }
  }
}
