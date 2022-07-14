package callete.deployment.server;

import callete.api.Callete;
import callete.api.util.SystemUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Starts the deployment server with the DeploymentResource REST service.
 */
public class DeploymentService {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentService.class);

  public static void main(String[] args) throws IOException {
    int port = Callete.getConfiguration().getInt("deployment.port", 8080);
    String host = Callete.getConfiguration().getString("deployment.host");
    if(StringUtils.isEmpty(host)) {
      host = SystemUtils.resolveHostAddress();
      LOG.warn("No 'deployment.host' set, resolved " + host);
    }
    Callete.getHttpService().startServer(host, port, new File("./"), new String[]{"callete.deployment"});

    LOG.info("Deployment Service is ready.");
    System.in.read();
  }
}
