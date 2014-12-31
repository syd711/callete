package callete.deployment.client;

import callete.api.util.SystemUtils;
import callete.deployment.server.Deployment;
import callete.deployment.server.DeploymentResource;
import callete.deployment.server.DeploymentStatus;
import callete.deployment.util.FileUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The http client that executes the REST request for the deployment.
 */
public class DeploymentHttpClient {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentHttpClient.class);
  private DeploymentDescriptor descriptor;

  public DeploymentHttpClient(DeploymentDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * Executes the given REST command
   *
   * @param cmd The REST call to execute.
   */
  public DeploymentStatus executeCommand(@Nonnull String cmd) {
    String url = descriptor.getRequestBasePath() + cmd;
    LOG.info("Deployment client executing command '" + url + "'");

    switch (cmd) {
      case Deployment.CMD_DESTROY: {
        return executeGetRequest(url);
      }
      case Deployment.CMD_CLEAN: {
        String ignoreDirectories = ""; //not used yet
        Map<String,String> params = new HashMap<>();
        params.put(DeploymentResource.PARAM_IGNORE_DIRECTORIES, ignoreDirectories);
        return executePostRequest(url, params);
      }
      case Deployment.CMD_CREATE: {
        String targetDirectory = descriptor.getTargetDirectory();
        Map<String,String> params = new HashMap<>();
        params.put(DeploymentResource.PARAM_TARGET_DIRECTORY, targetDirectory);
        return executePostRequest(url, params);
      }
      case Deployment.CMD_COPY: {
        try {
          File projectZipFile = descriptor.getSourceArchive();
          LOG.info("Copying " + projectZipFile.getAbsolutePath() + ", size: " + SystemUtils.humanReadableByteCount(projectZipFile.length()));
          return executeMultipartRequest(url, projectZipFile);
        } catch (Exception e) {
          LOG.error("Error copying project zip file ");
        }
        break;
      }
      case Deployment.CMD_RUN: {
        return executeGetRequest(url);
      }
    }
    return null;
  }

  /**
   * Executes a POST request with multipart request data.
   * We assume that the REST method called has a single request parameter called "file"
   * that accepts the given file that should be transferred.
   * @param url The REST method that accepts the request.
   * @param file The file that should be transferred.
   */
  protected DeploymentStatus executeMultipartRequest(String url, File file) {
    Client client = createClient();
    try {
      WebResource webResource = client.resource(url);
      final FormDataMultiPart multiPart = new FormDataMultiPart();
      multiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.valueOf("application/zip")));

      final ClientResponse clientResp = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
              ClientResponse.class, multiPart);

      LOG.info(url + ": MultiPart Response: " + clientResp.getClientResponseStatus());
      DeploymentStatus entity = clientResp.getEntity(DeploymentStatus.class);
      LOG.info(url + ": MultiPart Response Entity: " + entity.getErrorMessage());
      return entity;
    } catch (Exception e) {
      LOG.error("Error executing deployment command '" + url + "': " + e.getMessage(), e);
    } finally {
      client.destroy();
    }
    return null;
  }

  /**
   * Executes a regular GET request without params required.
   * @param url the REST command to execute, see DeploymentResource.
   */
  protected DeploymentStatus executeGetRequest(String url) {
    Client client = createClient();
    try {
      WebResource webResource = client.resource(url);
      final ClientResponse clientResp = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
      LOG.info(url + ": GET Response: " + clientResp.getClientResponseStatus());
      DeploymentStatus entity = clientResp.getEntity(DeploymentStatus.class);
      LOG.info(url + ": GET Response Entity: " + entity.getErrorMessage());
      return entity;
    } catch (Exception e) {
      LOG.error("Error executing deployment command '" + url + "': " + e.getMessage(), e);
    } finally {
      client.destroy();
    }
    return null;
  }

  /**
   * Executes a regular POST request with parameters.
   * @param url the REST command to execute, see DeploymentResource.
   */
  @SuppressWarnings("unchecked")
  protected DeploymentStatus executePostRequest(@Nonnull String url, @Nonnull Map<String,String> params) {
    Client client = createClient();
    try {
      WebResource webResource = client.resource(url);
      MultivaluedMap formData = new MultivaluedMapImpl();
      for (Map.Entry<String, String> next : params.entrySet()) {
        formData.put(next.getKey(), Arrays.asList(next.getValue()));
      }
      final ClientResponse clientResp = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
      LOG.info(url + ": POST Response Status: " + clientResp.getClientResponseStatus());
      DeploymentStatus entity = clientResp.getEntity(DeploymentStatus.class);
      LOG.info(url + ": POST Response Entity: " + entity.getErrorMessage());
      return entity;
    } catch (Exception e) {
      LOG.error("Error executing deployment command '" + url + "': " + e.getMessage(), e);
    } finally {
      client.destroy();
    }
    return null;
  }

  /**
   * Creates the Jersey client with POJO mapping
   */
  private static Client createClient() {
    ClientConfig clientConfig = new DefaultClientConfig();
    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    return Client.create(clientConfig);
  }
}
