package callete.api.services.music.player;

/**
 * Event listener fired when items on the playlist have changed.
 */
public interface PlaylistChangeListener {

  void playlistChanged(PlaylistChangeEvent e);
}
