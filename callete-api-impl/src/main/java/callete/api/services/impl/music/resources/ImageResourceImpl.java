package callete.api.services.impl.music.resources;

import callete.api.services.music.resources.ImageResource;

/**
 * Image resource implementation, used for caching, sizing and cropping.
 */
public class ImageResourceImpl implements ImageResource {
  private String url;

  public ImageResourceImpl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Image Resource '" + url + "'";
  }
}
