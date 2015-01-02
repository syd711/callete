package callete.api.services.music.model;

/**
 * Model used to playback internet radio streams.
 */
public class Stream implements PlaylistItem {
  private String name;
  private String url;

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

  @Override
  public String toString() {
    return "Stream '" + getPlaybackUrl() + "'";
  }
}