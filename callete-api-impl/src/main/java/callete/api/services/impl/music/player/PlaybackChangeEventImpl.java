package callete.api.services.impl.music.player;

import callete.api.Callete;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.PlaybackChangeEvent;

import java.util.List;

/**
 * Default implementation of the playback change event.
 */
public class PlaybackChangeEventImpl implements PlaybackChangeEvent {

  @Override
  public PlaylistItem getActiveItem() {
    return Callete.getMusicPlayer().getPlaylist().getActiveItem();
  }

  @Override
  public int getActiveIndex() {
    List<PlaylistItem> playlistItems = Callete.getMusicPlayer().getPlaylist().getPlaylistItems();
    PlaylistItem activeItem = Callete.getMusicPlayer().getPlaylist().getActiveItem();
    return playlistItems.indexOf(activeItem);
  }
}
