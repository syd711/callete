package callete.api.services.impl.http;

import callete.api.services.http.HttpService;
import callete.api.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the HttpService interface, holds all server instances.
 */
@SuppressWarnings("unused")
public class HttpServiceImpl implements HttpService {
  private final static Logger LOG = LoggerFactory.getLogger(HttpServiceImpl.class);

  private Map<Integer, GrizzlyHttpServer> servers = new HashMap<Integer, GrizzlyHttpServer>();

  @Override
  public void startServer(int port, File resourceDirectory, String[] resourcePackages) {
    startServer(SystemUtils.resolveHostAddress(), port, resourceDirectory, resourcePackages);
  }

  @Override
  public void startServer(String host, int port, File resourceDirectory, String[] resourcePackages) {
    GrizzlyHttpServer server = new GrizzlyHttpServer(host, port, resourceDirectory, resourcePackages);
    try {
      server.start();
      servers.put(port, server);
    } catch (Exception e) {
      LOG.error("Error starting " + server + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void stopServer(int port) {
    if (servers.containsKey(port)) {
      GrizzlyHttpServer server = servers.get(port);
      server.stop();
    } else {
      LOG.error("No server running on port " + port + " to stop.");
    }
  }
}
