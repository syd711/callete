package callete.api.services.impl.music.player;

import callete.api.Callete;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.PlaylistChangeEvent;

import java.util.List;

/**
 * Implementation of the PlaylistChangeEvent interface.
 */
public class PlaylistChangedEventImpl implements PlaylistChangeEvent {
  @Override
  public List<PlaylistItem> getPlaylistItems() {
    return Callete.getMusicPlayer().getPlaylist().getPlaylistItems();
  }

  @Override
  public PlaylistItem getActiveItem() {
    return Callete.getMusicPlayer().getPlaylist().getActiveItem();
  }
}
