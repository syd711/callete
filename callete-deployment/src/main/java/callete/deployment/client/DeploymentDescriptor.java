package callete.deployment.client;

import callete.api.Callete;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * The descriptor holds all data required for a deployment such
 * as the source directory and the target where to deploy the project.
 */
public class DeploymentDescriptor {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentDescriptor.class);

  private static List<String> DEFAULT_EXCLUSIONS = Arrays.asList(".idea", "bin", ".classpath", ".project", ".iml", ".md");
  private String targetDirectory;

  private String requestBasePath;
  private List<String> ignoreFiles;

  public DeploymentDescriptor(List<String> ignoreFiles) {
    this.ignoreFiles = ignoreFiles;
    this.ignoreFiles.addAll(DEFAULT_EXCLUSIONS);
    String host = Callete.getConfiguration().getString("deployment.host");
    String port = Callete.getConfiguration().getString("deployment.port", "8080");

    this.targetDirectory = Callete.getConfiguration().getString("deployment.targetDirectory");
    this.requestBasePath = "http://" + host + ":" + port + "/deployment/";

    LOG.info("Created " + this);
  }

  public String getTargetDirectory() {
    return targetDirectory;
  }

  public String getRequestBasePath() {
    return requestBasePath;
  }

  public List<String> getIgnoreFiles() {
    return ignoreFiles;
  }

  @Override
  public String toString() {
    return "Deployment Descriptor [targetDirectory:" + targetDirectory + ", requestBasePath:" + requestBasePath + "]\n" +
        "Ignore list: " + StringUtils.join(ignoreFiles, ", ");
  }
}
