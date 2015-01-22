package callete.api.services.system;

import callete.api.services.Service;

/**
 * Service to gather system information, like system disc usage.
 */
public interface SystemService extends Service {

  /**
   * Returns the number of unallocated bytes in the partition <a
   * href="#partName">named</a> by this abstract path name.
   * <p/>
   * <p> The returned number of unallocated bytes is a hint, but not
   * a guarantee, that it is possible to use most or any of these
   * bytes.  The number of unallocated bytes is most likely to be
   * accurate immediately after this call.  It is likely to be made
   * inaccurate by any external I/O operations including those made
   * on the system outside of this virtual machine.  This method
   * makes no guarantee that write operations to this file system
   * will succeed.
   *
   * @return The number of unallocated bytes on the partition or <tt>0L</tt>
   * if the abstract pathname does not name a partition.  This
   * value will be less than or equal to the total file system size
   */
  long getAvailableDiskSpace();

  /**
   * Returns the maximum amount of memory that the Java virtual machine will
   * attempt to use.  If there is no inherent limit then the value {@link
   * java.lang.Long#MAX_VALUE} will be returned.
   *
   * @return the maximum amount of memory that the virtual machine will
   * attempt to use, measured in bytes
   */
  long getMaxMemory();

  /**
   * Returns the total amount of memory in the Java virtual machine.
   * The value returned by this method may vary over time, depending on
   * the host environment.
   * <p/>
   * Note that the amount of memory required to hold an object of any
   * given type may be implementation-dependent.
   *
   * @return the total amount of memory currently available for current
   * and future objects, measured in bytes.
   */
  long getTotalMemory();

  long getUsedDiskSpace();

  /**
   * Returns the amount of free memory in the Java Virtual Machine.
   * Calling the
   * <code>gc</code> method may result in increasing the value returned
   * by <code>freeMemory.</code>
   *
   * @return an approximation to the total amount of memory currently
   * available for future allocated objects, measured in bytes.
   */
  long getFreeMemory();

  /**
   * Returns the alphanumeric name of the host
   */
  String getHostname();

  /**
   * Returns the IP address assigned to eth0 of this host.
   */
  String getHostAddress();

  /**
   * Returns the CPU usage in percentage.
   */
  double getCpuUsage();
}
