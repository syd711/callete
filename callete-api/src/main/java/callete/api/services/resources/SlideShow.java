package callete.api.services.resources;

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
