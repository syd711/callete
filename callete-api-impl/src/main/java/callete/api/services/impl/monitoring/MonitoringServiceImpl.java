package callete.api.services.impl.monitoring;

import callete.api.services.monitoring.MonitoringService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The implementation of the monitoring service.
 */
public class MonitoringServiceImpl implements MonitoringService {

  @Override
  public int httpPing(String ip) throws IOException {
    String host = ip;
    if(!host.startsWith("http://")) {
      host = "http://" + host;
    }
    URL url = new URL(host);
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setRequestMethod("GET");
    connection.connect();


    return connection.getResponseCode();
  }
}
