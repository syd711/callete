package callete.deployment.client;

import callete.api.Callete;

/**
 * The descriptor holds all data required for a deployment such
 * as the source directory and the target where to deploy the project.
 */
public class DeploymentDescriptor {
  private String targetDirectory;

  private String requestBasePath;

  public DeploymentDescriptor() {
    String host = Callete.getConfiguration().getString("deployment.host");
    String port = Callete.getConfiguration().getString("deployment.port", "80");

    this.targetDirectory = Callete.getConfiguration().getString("deployment.targetDirectory");
    this.requestBasePath = "http://" + host + ":" + port + "/deployment/";
  }

  public String getTargetDirectory() {
    return targetDirectory;
  }

  public String getRequestBasePath() {
    return requestBasePath;
  }
}
