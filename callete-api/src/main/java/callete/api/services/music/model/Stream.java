package callete.api.services.music.model;

/**
 * Model used to playback internet radio streams.
 */
public class Stream implements PlaylistItem {
  private String name;
  private String url;
  private int id;

  public void setName(String name) {
    this.name = name;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Stream '" + getPlaybackUrl() + "'";
  }
}