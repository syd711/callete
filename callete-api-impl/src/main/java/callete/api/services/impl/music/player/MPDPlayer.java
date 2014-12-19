package callete.api.services.impl.music.player;

import callete.api.Callete;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.MusicPlayerPlaylist;
import callete.api.util.SystemUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.bff.javampd.MPD;
import org.bff.javampd.events.MPDErrorEvent;
import org.bff.javampd.events.MPDErrorListener;
import org.bff.javampd.events.OutputChangeEvent;
import org.bff.javampd.events.OutputChangeListener;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps all music player action which are delegated by the MusicPlayerServiceImpl.
 */
public class MPDPlayer implements MPDErrorListener, OutputChangeListener {
  private final static Logger LOG = LoggerFactory.getLogger(MPDPlayer.class);

  public static final String MPD_STATE_PAUSED = "pause";
  public static final String MPD_STATE_PLAYING = "play";

  private MPD mpd;
  private MusicPlayerPlaylist playlist;
  private MPDPlaylistMonitor monitor;

  public MPDPlayer(MusicPlayerServiceImpl musicPlayerService, MusicPlayerPlaylist playlist) {
    this.playlist = playlist;
    this.monitor = new MPDPlaylistMonitor(musicPlayerService, this, playlist);
  }

  public boolean play() {
    PlaylistItem item = playlist.getActiveItem();
    if(item != null) {
      play(item);
      monitor.startMonitoring();
      return true;
    }
    return false;
  }

  public void stop() {
    try {
      monitor.stopMonitoring();
      mpd.getPlayer().stop();
    } catch (MPDPlayerException e) {
      LOG.error("Error stopping MPD player: " + e.getMessage(), e);
    }
  }

  public void pause() {
    try {
      mpd.getPlayer().pause();
    } catch (MPDPlayerException e) {
      LOG.error("Error pausing MPD player: " + e.getMessage(), e);
    }
  }

  public boolean next() {
    PlaylistItem item = playlist.next();
    if(item == null) {
      monitor.stopMonitoring();
      return false;
    }
    play(item);
    return true;
  }


  public boolean previous() {
    PlaylistItem item = playlist.previous();
    if(item == null) {
      monitor.stopMonitoring();
      return false;
    }
    play(item);
    return true;
  }

  public void play(PlaylistItem item) {
    try {
      LOG.info("Starting playback: " + item);
      MPDSong mpdSong = new MPDSong();
      mpdSong.setFile(item.getPlaybackUrl());
      mpd.getPlaylist().clearPlaylist();
      mpd.getPlaylist().addSong(mpdSong);
      mpd.getPlayer().play();
    } catch (MPDPlaylistException e) {
      LOG.error("MPD playlist exception: " + e.getMessage(), e);
    } catch (MPDPlayerException e) {
      LOG.error("MPD player exception: " + e.getMessage(), e);
    }
  }

  public MPD getClient() {
    return mpd;
  }

  /**
   * Connects to the MPD service and creates
   * the client.
   * @return true if the connection was successfully established.
   */
  public boolean connect() {
    try {
      Configuration configuration = Callete.getConfiguration();
      String host = configuration.getString("mpd.host");
      if(StringUtils.isEmpty(host)) {
        host = SystemUtils.resolveHostAddress();
      }
      int port = configuration.getInt("mpd.port", 6600);
      MPD.Builder builder = new MPD.Builder();
      mpd = builder.server(host).port(port).build();

      //register several listeners to be used for monitoring and playing.
      mpd.getMonitor().addMPDErrorListener(this);
      mpd.getMonitor().addOutputChangeListener(this);

      LOG.info("MPD Version:" + mpd.getVersion());
      return true;
    }
    catch (Exception e) {
      LOG.error("Failed to connect to MPD server: " + e.getMessage(), e);
    }
    return false;
  }

  // ------------------------ Monitoring ---------------------------------------

  public boolean isPlaying() {
    try {
      String state = mpd.getServerStatus().getState();
      return state.equals(MPD_STATE_PLAYING);
    } catch (MPDResponseException e) {
      LOG.error("Error during monitoring mpd status: " + e.getMessage(), e);
    }
    return false;
  }

  public boolean isPaused() {
    try {
      String state = mpd.getServerStatus().getState();
      return state.equals(MPD_STATE_PAUSED);
    } catch (MPDResponseException e) {
      LOG.error("Error during monitoring mpd status: " + e.getMessage(), e);
    }
    return false;
  }

  // --------------------- Event Handling --------------------------------------

  @Override
  public void errorEventReceived(MPDErrorEvent mpdErrorEvent) {
    LOG.error("MPD Error event: " + mpdErrorEvent.getMsg());
  }

  @Override
  public void outputChanged(OutputChangeEvent event) {
    System.out.println(event.getSource());
  }
}
