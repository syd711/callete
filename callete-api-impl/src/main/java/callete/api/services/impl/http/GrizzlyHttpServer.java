package callete.api.services.impl.http;

import com.google.common.base.Joiner;
import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.util.*;

/**
 * Manages the HTTP server.
 */
public class GrizzlyHttpServer {
  private final static Logger LOG = LoggerFactory.getLogger(GrizzlyHttpServer.class);

  private int port;
  private String host;
  private File resourceDirectory;
  private String[] resourcePackages;

  private org.glassfish.grizzly.http.server.HttpServer httpServer;

  public GrizzlyHttpServer(String host, int port, File resourceDirectory, String[] resourcePackages) {
    this.host = host;
    this.port = port;
    this.resourceDirectory = resourceDirectory;
    this.resourcePackages = resourcePackages;
  }

  /**
   * Starts the http server for the configured port.
   *
   * @throws IllegalArgumentException
   * @throws java.io.IOException
   */
  public void start() throws Exception {
    final Map<String, String> initParams = new HashMap<>();

    List<String> packageNames = new ArrayList<>(Arrays.asList(resourcePackages));
    packageNames.add("com.sun.jersey");
    initParams.put("com.sun.jersey.config.property.packages", Joiner.on(",").join(packageNames));
    initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

    URI url = UriBuilder.fromUri("http://" + host + "/").port(port).build();
    LOG.info("Starting server on: " + url.toString());

    httpServer = GrizzlyWebContainerFactory.create(url, initParams);
    httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler(resourceDirectory.getAbsolutePath()), "/resources");

    //TODO disabled cache since the wrong mime type is returned otherwise
    for(NetworkListener l : httpServer.getListeners()) {
      l.getFileCache().setEnabled(false);
    }
    httpServer.start();
    LOG.info("Started " + this);
  }

  public void stop() {
    httpServer.stop();
    LOG.info("Stopped " + this);
  }

  @Override
  public String toString() {
    return "HttpServer running on port " + port;
  }
}
