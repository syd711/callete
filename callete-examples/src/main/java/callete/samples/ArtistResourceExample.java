package callete.samples;

import callete.api.Callete;
import callete.api.services.music.resources.ArtistResourcesService;
import callete.api.services.music.resources.ImageResource;

/**
 * Example how to use the artist resource service.
 * The service is currently limited to images and depends on a echo nest developer api key.
 */
public class ArtistResourceExample {

  public static void main(String[] args) throws InterruptedException {
    ArtistResourcesService artistResourcesService = Callete.getArtistResourcesService();
    ImageResource imageResourceFor = artistResourcesService.getImageResourceFor("depeche mode");
    System.out.println(imageResourceFor);
  }
}
