package callete.api.services.music.model;

import callete.api.services.ServiceModel;

/**
 * Superclass for all model instances.
 */
public interface PlaylistItem extends ServiceModel {

  /**
   * The playback URL retrieval has to be implemented by the concrete instances.
   */
  public abstract String getPlaybackUrl();
}
