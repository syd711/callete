package callete.api.services.http;

import callete.api.services.Service;

import java.io.File;

/**
 * Service API interface for an embedded Java HTTP service that supports REST.
 */
public interface HttpService extends Service {

  /**
   * Will register all REST resources and play the service.
   */
  void startServer(String host, int port, File resourceDirectory, String[] resourcePackages);

  /**
   * Will register all REST resources and start the service.
   * The host name will be automatically resolved instead of using a fixed value.
   */
  void startServer(int port, File resourceDirectory, String[] resourcePackages);

  /**
   * The server to stop, identified by the port its running on.
   */
  void stopServer(int port);
}
