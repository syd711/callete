package callete.api.services.music.player;

import callete.api.services.music.model.PlaylistItem;

/**
 * Model used for meta data update events.
 */
public class PlaylistMetaData {
  private String name;
  private String artist;
  private String title;
  private PlaylistItem item;

  public PlaylistMetaData(PlaylistItem item, String name, String artist, String title) {
    this.item = item;
    this.artist = artist;
    this.title = title;
    this.name = name;
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

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    PlaylistMetaData compare = (PlaylistMetaData) obj;
    return item.getPlaybackUrl().equals(compare.getItem().getPlaybackUrl())
            && String.valueOf(title).equals(String.valueOf(compare.getTitle()))
            && String.valueOf(name).equals(String.valueOf(compare.getName()));
  }
}
