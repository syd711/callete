package callete.api.services.impl.music.player;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods for handling the MPD.
 */
public class MPDTelnetClient {
  private final static Logger LOG = LoggerFactory.getLogger(MPDTelnetClient.class);

  private TelnetClient client;
  private InputStream in = null;
  private String host;
  private int port;
  private MPDTelnetReader outputStream;

  public MPDTelnetClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * Creates a new telnet connection to the MPD.
   */
  public void connect() {
    try {
      if (in != null) {
        in.close();
      }
      if (client != null) {
        client.disconnect();
      }
      client = new TelnetClient();
      client.connect(host, port);

      outputStream = new MPDTelnetReader(client);
      outputStream.start();
      in = client.getInputStream();
      LOG.info("Initialized " + this);
    } catch (Exception e) {
      LOG.error("Failed to connect to " + this + ": " + e.getMessage());
    }
  }

  /**
   * Executes a MPD command via telnet.
   */
  public void executeTelnetCommand(String cmd) {
    try {
      cmd += "\n";
      if (client == null || client.getOutputStream() == null) {
        connect();
      }

      if (client.getOutputStream() != null) {
        client.getOutputStream().write(cmd.getBytes());
        client.getOutputStream().flush();
      }
      else {
        LOG.error("Exception executing MPD telnet command: Could not acquire telnet output steam, please check the MPD server connection.");
      }
    } catch (IOException e) {
      LOG.error("Exception executing MPD telnet command '" + cmd + "':" + e.getMessage());
      this.client = null;
    }
  }


  /**
   * Executes the playlistinfo command and applies
   * the result string to the current station.
   * @return the string result of the system command.
   */
  public String playlistInfo() {
    if(client != null && client.isConnected()) {
      synchronized (this) {
        executeTelnetCommand("playlistinfo");
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return outputStream.getLastCommand();
      }
    }
    else {
      LOG.error("Failed to retrieve mpc playlist info: " + this + " is not connected, trying to reconnect...");
      connect();
    }
    return null;
  }


  @Override
  public String toString() {
    return "MPD Client for " + host + ":" + port;
  }
}
