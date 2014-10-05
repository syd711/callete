package callete.api.services.music.model;

/**
 * Superclass for all model instances.
 */
public abstract class PlaylistItem {

  /**
   * The playback URL retrieval has to be implemented by the concrete instances.
   */
  public abstract String getPlaybackUrl();
}
