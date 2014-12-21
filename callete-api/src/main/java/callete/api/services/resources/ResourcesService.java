package callete.api.services.resources;

import callete.api.services.Service;

import java.io.File;

/**
 * Service for providing image and other resources for an artist.
 */
public interface ResourcesService extends Service {

  /**
   * Search for images of the artist with the given name.
   * The search result is reduced to one hit since the implementation
   * itself should decide which image to use.
   * @param artist The artist to search for.
   * @return An image resource.
   */
  ArtistResources getImageResourcesFor(String artist);

  /**
   * Returns a slide show object that contains the images located
   * in the given folder
   * @param folder the folder to read the images from, not recursively.
   */
  SlideShow getSlideShow(File folder);
}
