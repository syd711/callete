package callete.samples;

import callete.api.Callete;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

/**
 * This sample starts a http server on port 4444 and registers itself as REST resource
 * on subpath "/test". You can run the example and request the helloWorld() method
 * via http://localhost:4444/test
 */
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class HttpServiceExample {
  public static void main(String[] args) throws IOException {
    int serverPort = 4444;
    //the resource directory doesn't really matter here, since we only want to provide a REST service
    File resourceDirectory = new File("g:/temp");

    //the package that should be scanned for resources, so since this is a resource we add the parent package.
    String[] resourcesLookupPaths = {"de.gaia"};

    //finally start the HTTP server
    Callete.getHttpService().startServer(serverPort, resourceDirectory, resourcesLookupPaths);

    //and wait...
    System.in.read();
  }

  @GET
  public Test helloWorld() {
    return new Test();
  }

  /**
   * Simple POJO to show that the JSON conversion is working.
   */
  class Test {
    private String test = "hello world";

    public String getTest() {
      return test;
    }
  }
}