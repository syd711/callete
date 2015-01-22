package callete.api.services.impl.http;

import callete.api.services.http.HttpService;
import callete.api.util.SystemUtils;
import org.glassfish.grizzly.http.server.HttpServer;
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

  @Override
  public HttpServer startServer(int port, File resourceDirectory, String[] resourcePackages) {
    return startServer(SystemUtils.resolveHostAddress(), port, resourceDirectory, resourcePackages);
  }

  @Override
  public HttpServer startServer(String host, int port, File resourceDirectory, String[] resourcePackages) {
    GrizzlyHttpServer server = new GrizzlyHttpServer(host, port, resourceDirectory, resourcePackages);
    try {
      return server.start();
    } catch (Exception e) {
      LOG.error("Error starting " + server + ": " + e.getMessage(), e);
    }
    return null;
  }
}
