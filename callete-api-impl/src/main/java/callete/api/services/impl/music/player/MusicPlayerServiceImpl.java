package callete.api.services.impl.music.player;

import callete.api.services.music.player.MusicPlayerPlaylist;
import callete.api.services.music.player.MusicPlayerService;
import callete.api.services.music.player.PlaybackChangeEvent;
import callete.api.services.music.player.PlaybackChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation o the MusicPlayerService interface.
 * This implementation is using the linux MPD service for playback.
 * Corresponding actions are delegated to the MPDPlayer implementation.
 */
@SuppressWarnings("unused")
public class MusicPlayerServiceImpl implements MusicPlayerService {
  private final static Logger LOG = LoggerFactory.getLogger(MusicPlayerService.class);

  private MPDPlayer mpdPlayer;
  private MusicPlayerPlaylist playlist = new MusicPlayerPlaylistImpl();
  private List<PlaybackChangeListener> listeners = new ArrayList<>();

  @Override
  public void play() {
    LOG.info("Invoked MPD play");
    boolean play = getPlayer().play();
    if(play) {
      firePlaybackChangeEvent();
    }
  }

  @Override
  public void stop() {
    LOG.info("Invoked MPD stop");
    getPlayer().stop();
  }

  @Override
  public void pause() {
    LOG.info("Invoked MPD pause");
    mpdPlayer.pause();
  }

  @Override
  public boolean next() {
    LOG.info("Invoked MPD next");
    boolean next = getPlayer().next();
    if(next) {
      firePlaybackChangeEvent();
    }
    return next;
  }

  @Override
  public boolean previous() {
    LOG.info("Invoked MPD previous");
    boolean previous = getPlayer().previous();
    if(previous) {
      firePlaybackChangeEvent();
    }
    return previous;
  }

  @Override
  public void addPlaybackChangeEventListener(PlaybackChangeListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removePlaybackChangeEventListener(PlaybackChangeListener listener) {
    listeners.remove(listener);
  }

  @Override
  public MusicPlayerPlaylist getPlaylist() {
    return playlist;
  }


  // -------------------------- Helper --------------------------------------

  private MPDPlayer getPlayer() {
    if(mpdPlayer == null) {
      mpdPlayer = new MPDPlayer(this, playlist);
      mpdPlayer.connect();
    }
    return mpdPlayer;
  }

  private void firePlaybackChangeEvent() {
    PlaybackChangeEvent e = new PlaybackChangeEventImpl();
    for(PlaybackChangeListener l : listeners) {
      l.playbackChanged(e);
    }
  }
}
