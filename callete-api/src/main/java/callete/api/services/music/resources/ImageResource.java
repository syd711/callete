package callete.api.services.music.resources;

import java.awt.image.BufferedImage;

/**
 * Image wrapper to retrieve format metadata used
 * during scaling and cropping.
 */
public interface ImageResource {
  public String getUrl();

  public String getImageFormat();

  public BufferedImage getImage();
}

