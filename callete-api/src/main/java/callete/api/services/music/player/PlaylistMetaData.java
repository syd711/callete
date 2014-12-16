package callete.api.services.music.player;

import callete.api.services.music.model.PlaylistItem;

/**
 * Model used for meta data update events.
 */
public class PlaylistMetaData {
  private String artist;
  private String title;
  private PlaylistItem item;

  public PlaylistMetaData(PlaylistItem item, String artist, String title) {
    this.item = item;
    this.artist = artist;
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public String getTitle() {
    return title;
  }

  public PlaylistItem getItem() {
    return item;
  }
}
