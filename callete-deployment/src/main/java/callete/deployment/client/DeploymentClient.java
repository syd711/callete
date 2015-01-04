package callete.deployment.client;

import callete.deployment.server.Deployment;
import callete.deployment.server.DeploymentStatus;
import callete.deployment.util.DeploymentArchiver;
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
    String artifactId = args[0];
    String version = args[1];
    boolean quickDeployment = Boolean.parseBoolean(args[2]);

    DeploymentDescriptor descriptor = new DeploymentDescriptor(artifactId, version, quickDeployment);
    DeploymentArchiver deploymentArchiver = DeploymentArchiver.create(args);
    deploymentArchiver.setHost(descriptor.getHost());
    deploymentArchiver.setTargetDeploymentDir(descriptor.getTargetDirectory());
    deploymentArchiver.generateScript();


    new DeploymentClient().deploy(descriptor);
  }

  public void deploy(DeploymentDescriptor descriptor) throws Exception {

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
