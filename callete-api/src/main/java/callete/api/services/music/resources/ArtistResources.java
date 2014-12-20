package callete.api.services.music.resources;

/**
 * Interface to be implemented by services that provide artist data.
 */
public interface ArtistResources {
  /**
   * Returns a random image from the resources this wrapper contains.
   * The image is scaled to the required size so that the requested crop is cropped afterwards.
   * @param width the width of the returned image
   * @param height the height of the returned image
   * @param minImageSize the minimum width or height of the image to choose, select value <= 0 to ignore this param
   * @return an ImageResource that contains the image with the expected size.
   */
  ImageResource getRandomImage(int width, int height, int minImageSize);

  /**
   * Returns a random image from this resources container.
   * @param minImageSize the minimum width or height of the image to choose, select value <= 0 to ignore this param
   */
  ImageResource getRandomImage(int minImageSize);

  /**
   * The name of the artist this resource package has been created for.
   * @return the name of the artist.
   */
  String getArtist();

  /**
   * Returns true when no resources have been found for the artist.
   */
  boolean isEmpty();
}
