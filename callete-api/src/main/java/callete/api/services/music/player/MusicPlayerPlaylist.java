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
   * Sets the given item from the playlist as active item.
   *
   * @param item
   */
  void setActiveItem(PlaylistItem item);

  /**
   * Queues all songs of the given playlist to the addItem.
   */
  void setPlaylist(Playlist playlist);

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
   * Removes the change listener from the playlist.
   *
   * @param listener the listener to remove
   */
  void removeChangeListener(PlaylistChangeListener listener);

  /**
   * Adds a meta data change listener to the playlist. The listener is notified
   * when the meta data of the current playlist item is updated, e.g. the title of a radio stream.
   *
   * @param listener The listener to add.
   */
  void addMetaDataChangeListener(PlaylistMetaDataChangeListener listener);

  /**
   * Removes a meta data change listener to the playlist.
   *
   * @param listener The listener to remove.
   */
  void removeMetaDataChangeListener(PlaylistMetaDataChangeListener listener);

  /**
   * Updates the meta data of the playlist, fired the changes listeners afterwards.
   */
  void updateMetaData(PlaylistMetaData metaData);

  /**
   * Returns all item on the getActiveItem playlist.
   */
  List<PlaylistItem> getPlaylistItems();
}
