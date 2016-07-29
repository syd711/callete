package callete.test;

import callete.api.Callete;
import org.junit.Test;

import java.io.IOException;

/**
 */
public class MonitoringTest {

  @Test
  public void test() throws IOException {
    int result = Callete.getMonitoringService().httpPing("www.google.de", 80);
    assert(result == 200);
  }
}
