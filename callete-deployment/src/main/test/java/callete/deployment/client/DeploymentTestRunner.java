package callete.deployment.client;

import callete.api.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class DeploymentTestRunner {
  public static void main(String[] args) throws Exception {
    DeploymentHttpClient c = new DeploymentHttpClient(null);
    Map<String, String> params = new HashMap<>();
    params.put("target", "g:/temp/callete/");
    c.executeGetRequest("http://localhost:8099/deployment/destroy");
    c.executePostRequest("http://localhost:8099/deployment/create", params);
    c.executeGetRequest("http://localhost:8099/deployment/clean");

    File projectZipFile = File.createTempFile("callete_deployment", ".zip", new File(System.getProperty("java.io.tmpdir")));
    FileUtils.zipFolder(new File("G:/temp/srctest"), projectZipFile, new String[]{});
    c.executeMultipartRequest("http://localhost:8099/deployment/copy", projectZipFile);
    c.executeGetRequest("http://localhost:8099/deployment/run");
    System.exit(0);
  }
}
