package callete.deployment.server;

import java.util.Date;

/**
 * JSON pojo that contains the current deployment status.
 */
public class DeploymentStatus {
  private String tmpFile;
  private Date startTime = new Date();

  private String deploymentDirectory;
  private String errorMessage;

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public String getTmpFile() {
    return tmpFile;
  }

  public void setTmpFile(String tmpFile) {
    this.tmpFile = tmpFile;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getDeploymentDirectory() {
    return deploymentDirectory;
  }

  public void setDeploymentDirectory(String deploymentDirectory) {
    this.deploymentDirectory = deploymentDirectory;
  }
}
