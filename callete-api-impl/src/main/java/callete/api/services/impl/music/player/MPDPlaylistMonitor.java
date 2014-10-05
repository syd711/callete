package callete.api.services.impl.music.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors if the MPD is still playing, triggers the next song from the playlist.
 * Unfortunately we can't use the native MPD playlist including the event handling here since
 * every song url has to be requested when song song should be played.
 */
public class MPDPlaylistMonitor extends Thread {
  private final static Logger LOG = LoggerFactory.getLogger(MPDPlayer.class);

  private final static int POLL_INTERVAL = 2000;

  private boolean running = true;
  private boolean monitoring = false;
  private MPDPlayer player;

  public MPDPlaylistMonitor(MPDPlayer player) {
    super("MPD Playlist Monitoring Thread");
    this.player = player;
    this.start();
  }

  @Override
  public void run() {
    try {
      while (running) {
        if(monitoring) {
          if (!player.isPlaying() && !player.isPaused()) {
            player.next();
          }
        }
        Thread.sleep(POLL_INTERVAL);
      }
    } catch (InterruptedException e) {
      LOG.error("Error in MPD monitoring thread: " + e.getMessage(), e);
    }
  }

  public void startMonitoring() {
    this.monitoring = true;
  }

  public void stopMonitoring() {
    this.monitoring = false;
  }

}
