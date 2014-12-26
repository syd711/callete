package callete.api.services.music.player;

/**
 * Listener to be implemented to listen on playback changes.
 */
public interface PlaybackChangeListener {

  /**
   * Fired when a new item is played.
   */
  void playbackChanged(PlaybackChangeEvent event);
}
