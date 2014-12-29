package callete.deployment.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * The REST handler for deploying the archived maven module that should be deployed.
 */
@Path("/deployment")
@Produces(MediaType.APPLICATION_JSON)
public class DeploymentResource {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentResource.class);
  public final static String PARAM_TARGET_DIRECTORY = "target";
  public final static String PARAM_IGNORE_DIRECTORIES = "ignoreDirectories";

  //there can only be one deployment at a time, so a static instance is sufficient here.
  private static Deployment deployment;

  @POST
  @Path(Deployment.CMD_CREATE)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public DeploymentStatus create(@FormParam(PARAM_TARGET_DIRECTORY) String target) {
    LOG.info("Creating new deployment for directory '" + target + "'");
    deployment = new Deployment(target);
    return deployment.create();
  }

  @GET
  @Path(Deployment.CMD_DESTROY)
  public DeploymentStatus destroy() {
    if(deployment != null) {
      deployment.destroy();
      LOG.info("Destroyed deployment located in directory '" + deployment.getStatus().getDeploymentDirectory() + "'");
    }
    return new DeploymentStatus();
  }

  @POST
  @Path(Deployment.CMD_CLEAN)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public DeploymentStatus clean(@FormParam(PARAM_IGNORE_DIRECTORIES) String ignoreDirectories) {
    List<String> ignoreList = Arrays.asList(ignoreDirectories.split(","));
    deployment.clean(ignoreList);
    LOG.info("Clean up deployment located in directory '" + deployment.getStatus().getDeploymentDirectory() + "', waiting for archive to copy...");
    return deployment.getStatus();
  }

  @POST
  @Path(Deployment.CMD_COPY)
  @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM})
  public DeploymentStatus copy(File deploymentArchive) {
    return deployment.copy(deploymentArchive);
  }

  @GET
  @Path(Deployment.CMD_RUN)
  public DeploymentStatus run() {
    return deployment.run();
  }
}
