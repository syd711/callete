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

  public boolean isValid() {
    return !String.valueOf(artist).equals("0")
        && !String.valueOf(title).equals("0")
        && !String.valueOf(name).equals("0")
        && !String.valueOf(title).equals(String.valueOf(artist));
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

  @Override
  public String toString() {
    return "Meta data for Stream '" + name + "': " + artist + " - " + title;
  }
}
