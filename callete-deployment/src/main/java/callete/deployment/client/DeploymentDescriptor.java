package callete.deployment.client;

import callete.api.Callete;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * The descriptor holds all data required for a deployment such
 * as the source directory and the target where to deploy the project.
 */
public class DeploymentDescriptor {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentDescriptor.class);
  private String targetDirectory;

  private String requestBasePath;

  private File sourceArchive;

  public DeploymentDescriptor(String artifactId, String version) {
    sourceArchive = new File("./", artifactId + "-" + version + ".zip");
    String host = Callete.getConfiguration().getString("deployment.host");
    String port = Callete.getConfiguration().getString("deployment.port", "8080");

    this.targetDirectory = Callete.getConfiguration().getString("deployment.targetDirectory");
    this.requestBasePath = "http://" + host + ":" + port + "/deployment/";

    LOG.info("Created " + this);
  }

  public File getSourceArchive() {
    return sourceArchive;
  }

  public String getTargetDirectory() {
    return targetDirectory;
  }

  public String getRequestBasePath() {
    return requestBasePath;
  }

  @Override
  public String toString() {
    return "Deployment Descriptor [targetDirectory:" + targetDirectory + ", requestBasePath:"
        + requestBasePath + ", sourceArchive: " + sourceArchive.getAbsolutePath() + "]";
  }
}
