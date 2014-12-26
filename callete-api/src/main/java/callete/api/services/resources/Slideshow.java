package callete.api.services.resources;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Model for wrapping slide show images.
 */
public interface SlideShow {

  /**
   * Returns the next image for the slide show.
   */
  public ImageResource nextImage();

  /**
   * Returns the size of images in the slide show.
   */
  public int size();
}
