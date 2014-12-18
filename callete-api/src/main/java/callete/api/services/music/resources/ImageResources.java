package callete.api.services.music.resources;

/**
 * Interface to be implemented by services that provide artist image data.
 */
public interface ImageResources {
  /**
   * Returns a random image from the resources this wrapper contains.
   * The image is scaled to the required size so that the requested crop is cropped afterwards.
   * @param width the width of the returned image
   * @param height the height of the returned image
   * @return an ImageResource that contains the image with the expected size.
   */
  ImageResource getRandomImage(int width, int height);

  /**
   * Returns a random image from this resources container.
   */
  ImageResource getRandomImage();
}
