package callete.api.services.music.resources;

import callete.api.services.Service;

/**
 * Service for providing image and other resources for an artist.
 */
public interface ArtistResourcesService extends Service {

  /**
   * Search for images of the artist with the given name.
   * The search result is reduced to one hit since the implementation
   * itself should decide which image to use.
   * @param artist The artist to search for.
   * @return An image resource.
   */
  ArtistResources getImageResourcesFor(String artist);
}
