package callete.deployment.client;

import callete.deployment.server.Deployment;
import callete.deployment.server.DeploymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Main class to trigger the deployment.
 */
public class DeploymentClient {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentClient.class);

  public static void main(String[] args) throws Exception {
    String[] ignoreFiles = {};
    if(args != null && args.length > 0) {
      ignoreFiles = args[0].split(",");
    }
    new DeploymentClient().deploy(ignoreFiles);
  }

  public void deploy(String[] ignoreFiles) throws Exception {
    DeploymentDescriptor descriptor = new DeploymentDescriptor(Arrays.asList(ignoreFiles));
    DeploymentHttpClient client = new DeploymentHttpClient(descriptor);

    List<String> cmds = Arrays.asList(Deployment.CMD_DESTROY, Deployment.CMD_CREATE,
            Deployment.CMD_CLEAN, Deployment.CMD_COPY, Deployment.CMD_RUN);

    for(String cmd : cmds) {
      DeploymentStatus status = client.executeCommand(cmd);
      if(status == null) {
        throw new Exception("Deployment failed for unkown reasons, check log for details");
      }
      if(status.getErrorMessage() != null) {
        throw new Exception("Deployment failed: " + status.getErrorMessage());
      }
    }
    LOG.info("Deployment successful.");
  }
}
