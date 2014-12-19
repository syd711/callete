package callete.api.services.impl.music.resources;

import callete.api.Callete;
import callete.api.services.music.resources.ArtistResourcesService;
import callete.api.services.music.resources.ImageResources;
import com.echonest.api.v4.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
  public ImageResources getImageResourcesFor(String name) {
    try {
      Params p = new Params();
      p.add("name", name);
      p.add("results", 1);

      LOG.info("Requesting image resources for artist '" + name + "'");
      List<Artist> artists = api.searchArtists(p);
      for (Artist artist : artists) {
        List<Image> images = artist.getImages();
        LOG.info("Created image resource with " + images.size() + " images for artist '" + name + "'");
        return new ImageResourcesImpl(name, images);
      }
      if (artists.isEmpty()) {
        LOG.info("No images found for artist '" + name + "'");
      }
    } catch (EchoNestException e) {
      LOG.error("Error searching for artists resources: " + e.getMessage()
              + " (seems that happens sometimes, but the next request is successful afterwards.");
    }
    return new ImageResourcesImpl(name, Collections.<Image>emptyList());
  }
}
