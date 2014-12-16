package callete.api.services.impl.music.player;

import callete.api.Callete;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.PlaylistChangeEvent;

import java.util.List;

/**
 * Implementation of the PlaylistChangeEvent interface.
 */
public class PlaylistChangedEventImpl implements PlaylistChangeEvent {

  private List<PlaylistItem> items;
  private PlaylistItem activeItem;

  public PlaylistChangedEventImpl(List<PlaylistItem> items, PlaylistItem activeItem) {
    this.items = items;
    this.activeItem = activeItem;
  }

  @Override
  public List<PlaylistItem> getPlaylistItems() {
    return items;
  }

  @Override
  public PlaylistItem getActiveItem() {
    return activeItem;
  }
}
