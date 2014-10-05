package callete.api.services.music.player;

import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.PlaylistItem;

import java.util.List;

/**
 * Playlist interface for the music player.
 * Contains play lists and songs the user has selected for playback.
 */
public interface MusicPlayerPlaylist {

  /**
   * Returns the getActiveItem item from the playlist.
   */
  PlaylistItem getActiveItem();

  /**
   * Adds a item to the add.
   * @param item the item to add to the addItem.
   */
  void addItem(PlaylistItem item);

  /**
   * Adds a list of items to the playlist.
   * @param items the items to add
   */
  void addItems(List<? extends PlaylistItem> items);

  /**
   * Queues all songs of the given playlist to the addItem.
   */
  void addPlaylist(Playlist playlist);

  /**
   * Removes all songs from the playlist.
   */
  public void clear();

  /**
   * Returns the next song from the playlist or null if the end of the playlist has been reached.
   */
  PlaylistItem next();

  /**
   * Returns the previous song from the playlist or null if the end of the playlist has been reached.
   */
  PlaylistItem previous();

  /**
   * Adds a change listener to the playlist to listen for playlist changes.
   */
  void addChangeListener(PlaylistChangeListener listener);

  /**
   * Returns all item on the getActiveItem playlist.
   */
  List<PlaylistItem> getPlaylistItems();
}
