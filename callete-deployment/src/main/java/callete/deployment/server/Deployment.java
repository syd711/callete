package callete.deployment.server;

import callete.api.Callete;
import callete.api.util.SystemUtils;
import callete.deployment.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The actual deployment actions, like unpacking the deployment archive, building and installing it.
 */
public class Deployment {
  private final static Logger LOG = LoggerFactory.getLogger(Deployment.class);

  public final static String CMD_CREATE = "create";
  public final static String CMD_DESTROY = "destroy";
  public final static String CMD_CLEAN = "clean";
  public final static String CMD_RUN = "run";
  public final static String CMD_COPY = "copy";

  private DeploymentStatus status = new DeploymentStatus();
  private Process deployedProcess;

  public Deployment(String target) {
    status.setDeploymentDirectory(target);
  }

  /**
   * Ensures that the new deployment can be executed
   * in the given target directory.
   *
   * @return The current deployment status
   */
  public DeploymentStatus create() {
    if (status.getDeploymentDirectory() == null) {
      status.setErrorMessage("Deployment directory has not been set, check log for details.");
      return status;
    }

    File target = new File(status.getDeploymentDirectory());
    target.mkdirs();
    if (!target.exists()) {
      status.setErrorMessage("Could not create deployment directory '" + status.getDeploymentDirectory() + ", " +
              "please create it manually.");
    }
    return status;
  }

  public DeploymentStatus getStatus() {
    return status;
  }

  /**
   * Destroys the forked process this deployment is running.
   */
  public DeploymentStatus destroy() {
    if(deployedProcess != null) {
      deployedProcess.destroy();
    }
    return status;
  }

  /**
   * Cleans up the target folder that is stored in the DeploymentStatus.
   * A clean install is ensured for the next deployment this way.
   */
  public DeploymentStatus clean() {
    try {
      File targetDirectory = new File(status.getDeploymentDirectory());
      FileUtils.deleteFolder(targetDirectory);
    } catch (IOException e) {
      LOG.error("Error during deployment cleanup: " + e.getMessage());
      status.setErrorMessage(e.getMessage());
    }

    return status;
  }

  /**
   * Unpacks the deployment archive into the target directory.
   *
   * @param deploymentArchive the archive that contains the maven project to build.
   */
  public DeploymentStatus copy(File deploymentArchive) {
    try {
      deploymentArchive.deleteOnExit();
      LOG.info("Created deployment tmp file " + deploymentArchive.getAbsolutePath());
      status.setTmpFile(deploymentArchive.getAbsolutePath());
      FileUtils.unzip(deploymentArchive, new File(status.getDeploymentDirectory()));
    } catch (IOException e) {
      LOG.error("Error unpacking deployment archive: " + e.getMessage());
      status.setErrorMessage(e.getMessage());
    }

    return status;
  }

  /**
   * Runs the maven target that will start the deployed programm
   */
  public DeploymentStatus run() {
    executeMaven("execute");
    return status;
  }


  /**
   * Executes a maven target, used to build and run a deployed project
   * Unfortunately we can't use the apache maven invoker API here since
   * the API does not provide control over the running process which
   * we have to terminate for each deployment.
   *
   * @param profile The profile the build should be executed with, we use a profile to define the run configuration
   *                inside the deployed project. Check the gaia-template's pom.xml for details.
   * @return true if the maven execution was successful. The exit code of the command is checked for this too.
   */
  private boolean executeMaven(String profile) {
    try {
      String mavenHome = null;
      List<String> cmds = new ArrayList<>();
      if(SystemUtils.isWindows()) {
        cmds.add("mvn.bat");
      }
      else {
        cmds.add("sudo");
        //retrieve shell for execution.
        mavenHome = Callete.getConfiguration().getString("deployment.linux.maven.home");
        String mavenCmd = mavenHome;
        if(!mavenCmd.endsWith("/")) {
          mavenCmd+="/";
        }
        mavenCmd+="bin/mvn";
        cmds.add(mavenCmd);
      }

      //collection the basic commands for the maven build and run
      cmds.add("install");
      cmds.add("-Pexecute");

      LOG.info("Executing maven build in directory " + status.getDeploymentDirectory());
      LOG.info("Executing maven build: " + StringUtils.join(cmds, " "));
      final ProcessBuilder processBuilder = new ProcessBuilder(cmds).inheritIO().redirectErrorStream(true);
      if(mavenHome != null) {
        Map<String, String> environment = processBuilder.environment();
        environment.put("M2_HOME", mavenHome);
      }

      //determine the directory the maven call should be executed.
      File deploymentDirectory = new File(status.getDeploymentDirectory());
      processBuilder.directory(deploymentDirectory);

      //execute the process build
      deployedProcess = processBuilder.start();
    } catch (Exception e) {
      LOG.error("Error installing deployment: " + e.getMessage());
      status.setErrorMessage(e.getMessage());
      return false;
    }
    return true;
  }
}
