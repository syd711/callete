package callete.api.services.music.model;

/**
 * Model used to playback internet radio streams.
 */
public class Stream implements PlaylistItem {
  private String artist;
  private String title;
  private String name;
  private String url;

  public void setName(String name) {
    this.name = name;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get artist using stream's title
   */
  public String getArtist() {
    return artist;
  }

  /**
   * Get title using stream's title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the name of the stream.
   */
  public String getName() {
    return name;
  }

  @Override
  public String getPlaybackUrl() {
    return url;
  }

  @Override
  public String toString() {
    return "Stream '" + getPlaybackUrl() + "'";
  }
}