package callete.api.services.impl.music.player;

import callete.api.services.music.player.MusicPlayerPlaylist;
import callete.api.services.music.player.MusicPlayerService;
import callete.api.services.music.player.PlaylistMetaData;
import org.bff.javampd.objects.MPDSong;
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
  private MusicPlayerService player;
  private MPDPlayer mpdPlayer;
  private MusicPlayerPlaylist playlist;

  public MPDPlaylistMonitor(MusicPlayerService player, MPDPlayer mpdPlayer, MusicPlayerPlaylist playlist) {
    super("MPD Playlist Monitoring Thread");
    this.player = player;
    this.mpdPlayer = mpdPlayer;
    this.playlist = playlist;
    this.start();
  }

  @Override
  public void run() {
    try {
      while (running) {
        if (monitoring) {
          if (!mpdPlayer.isPlaying() && !mpdPlayer.isPaused()) {
            player.next();
          }
        }
        //sleep for the defined monitoring interval
        Thread.sleep(POLL_INTERVAL);

        MPDSong currentSong = mpdPlayer.getClient().getPlayer().getCurrentSong();
        if (currentSong != null) {
          PlaylistMetaData metaData = MPDMetaDataFactory.createMetaData(playlist.getActiveItem(), currentSong);
          if (metaData != null) {
            playlist.updateMetaData(metaData);
          }
        }
      }
    } catch (Exception e) {
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
