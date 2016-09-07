package callete.api.services.monitoring;

import callete.api.services.Service;

import java.io.IOException;

/**
 * Service interface for the monitoring service
 */
public interface MonitoringService extends Service {

  /**
   * Executes a ping for the given host and port.
   * Returns the http code of the ping request
   * @param ip the host name or ip of the service to ping
   * @return the HTTP status code of the request
   */
  int httpPing(String ip) throws IOException;
}
