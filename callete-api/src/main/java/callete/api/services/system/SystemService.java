package callete.api.services.system;

import callete.api.services.Service;

/**
 * Service to gather system information, like system disc usage.
 */
public interface SystemService extends Service {

  String getUsedDiskSpace();

  String getAvailableDiskSpace();

  String getHostname();

  String getHostAddress();
}
