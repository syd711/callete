package callete.deployment.client;

import callete.api.Callete;

import java.util.Arrays;
import java.util.List;

/**
 * The descriptor holds all data required for a deployment such
 * as the source directory and the target where to deploy the project.
 */
public class DeploymentDescriptor {
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
}
