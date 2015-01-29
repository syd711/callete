package callete.test;

import callete.api.Callete;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Matthias on 18.01.2015.
 */
public class HotSpotTest {

  @Test
  public void testHotSpot() throws IOException {
    Callete.getHttpService().startServer(8088, new File("./ui/hotspot/"), new String[]{"callete.api.services.impl.network"});

    System.in.read();
  }
}
