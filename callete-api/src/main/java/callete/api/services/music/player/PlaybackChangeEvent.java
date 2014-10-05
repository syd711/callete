package callete.api.services.music.player;

import callete.api.services.music.model.PlaylistItem;

/**
 * Event interface to be implemented to detected changes in of playback items.
 */
public interface PlaybackChangeEvent {

  /**
   * Returns the new active item the player is currently playing.
   */
  PlaylistItem getActiveItem();

  /**
   * Returns the numeric position of the active item inside the playlist.
   */
  int getActiveIndex();
}
