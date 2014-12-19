package callete.api.services.impl.music.player;

import callete.api.services.music.player.MusicPlayerPlaylist;
import callete.api.services.music.player.MusicPlayerService;
import callete.api.services.music.player.PlaybackChangeEvent;
import callete.api.services.music.player.PlaybackChangeEventListener;
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
  private MusicPlayerPlaylist playlist;
  private List<PlaybackChangeEventListener> listeners = new ArrayList<>();

  public MusicPlayerServiceImpl() {
    init();
  }

  @Override
  public void play() {
    LOG.info("Invoked MPD play");
    boolean play = mpdPlayer.play();
    if(play) {
      firePlaybackChangeEvent();
    }
  }

  @Override
  public void stop() {
    LOG.info("Invoked MPD stop");
    mpdPlayer.stop();
  }

  @Override
  public void pause() {
    LOG.info("Invoked MPD pause");
    mpdPlayer.pause();
  }

  @Override
  public boolean next() {
    LOG.info("Invoked MPD next");
    boolean next = mpdPlayer.next();
    if(next) {
      firePlaybackChangeEvent();
    }
    return next;
  }

  @Override
  public boolean previous() {
    LOG.info("Invoked MPD previous");
    boolean previous = mpdPlayer.previous();
    if(previous) {
      firePlaybackChangeEvent();
    }
    return previous;
  }

  @Override
  public void addPlaybackChangeEventListener(PlaybackChangeEventListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removePlaybackChangeEventListener(PlaybackChangeEventListener listener) {
    listeners.remove(listener);
  }

  @Override
  public MusicPlayerPlaylist getPlaylist() {
    return playlist;
  }


  // -------------------------- Helper --------------------------------------

  private void init() {
    playlist = new MusicPlayerPlaylistImpl();

    mpdPlayer = new MPDPlayer(this, playlist);
    mpdPlayer.connect();
  }

  private void firePlaybackChangeEvent() {
    PlaybackChangeEvent e = new PlaybackChangeEventImpl();
    for(PlaybackChangeEventListener l : listeners) {
      l.playbackChanged(e);
    }
  }
}
