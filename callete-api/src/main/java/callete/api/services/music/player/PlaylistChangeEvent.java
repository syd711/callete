package callete.api.services.music.player;

import callete.api.services.music.model.PlaylistItem;

import java.util.List;

/**
 * Event fired when the playlist has changed.
 */
public interface PlaylistChangeEvent {

  /**
   * Returns the getActiveItem items on the playlist.
   */
  List<PlaylistItem> getPlaylistItems();

  /**
   * Returns the items that is currently played from the active playlist.
   */
  PlaylistItem getActiveItem();
}
