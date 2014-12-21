package callete.api.services.resources;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Model for wrapping slide show images.
 */
public interface SlideShow {

  /**
   * Returns the images to be used for the slideshow.
   */
  List<BufferedImage> getImages();
}
