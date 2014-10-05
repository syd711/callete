package callete.api.services.impl.music.resources;

import callete.api.Callete;
import com.echonest.api.v4.*;
import callete.api.services.music.resources.ArtistResourcesService;
import callete.api.services.music.resources.ImageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of the ArtistResourceService interface.
 * We are using the Echo Nest API here, ensure that the developer key
 * is provided in the configuration properties.
 */
@SuppressWarnings("unused")
public class ArtistResourceServiceImpl implements ArtistResourcesService {
  private final static Logger LOG = LoggerFactory.getLogger(ArtistResourceServiceImpl.class);

  private EchoNestAPI api;

  public ArtistResourceServiceImpl() {
    super();
    try {
      String key = Callete.getConfiguration().getString("echo.nest.key");
      System.setProperty("ECHO_NEST_API_KEY", key);
      api = new EchoNestAPI();
      api.setTraceSends(false);
      api.setTraceRecvs(false);
    } catch (EchoNestException e) {
      LOG.error("Error connecting to echo nest service: " + e.getMessage(), e);
    }
  }

  @Override
  public ImageResource getImageResourceFor(String name) {
    try {
      Params p = new Params();
      p.add("name", name);
      p.add("results", 1);

      List<Artist> artists = api.searchArtists(p);
      for (Artist artist : artists) {
        List<Image> images = artist.getImages();
        for (int i = 0; i < images.size(); i++) {
          Image image = images.get(i);
          if(!image.getURL().contains("wikimedia")) {
            return new ImageResourceImpl(image.getURL());
          }

        }
      }
    } catch (EchoNestException e) {
      LOG.error("Error searching for artists resources: " + e.getMessage(), e);
    }
    return null;
  }
}
