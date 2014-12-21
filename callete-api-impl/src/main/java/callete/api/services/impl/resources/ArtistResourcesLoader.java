package callete.api.services.impl.resources;

import callete.api.Callete;
import callete.api.services.resources.ArtistResources;
import com.echonest.api.v4.*;
import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Guava cache loader for the EchoNest API calls.
 */
public class ArtistResourcesLoader extends CacheLoader<String, ArtistResources> {
  private final static Logger LOG = LoggerFactory.getLogger(ArtistResourcesLoader.class);

  private EchoNestAPI api;

  public ArtistResourcesLoader() {
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
  public ArtistResources load(String name) throws Exception {
    try {
      Params p = new Params();
      p.add("name", name);
      p.add("results", 1);

      LOG.info("Requesting image resources for artist '" + name + "'");
      List<Artist> artists = api.searchArtists(p);
      for (Artist artist : artists) {
        List<Image> images = artist.getImages();
        LOG.info("Created image resource with " + images.size() + " images for artist '" + name + "'");
        return new ArtistResourcesImpl(name, images);
      }
      if (artists.isEmpty()) {
        LOG.info("No images found for artist '" + name + "'");
      }
    } catch (EchoNestException e) {
      LOG.error("Error searching for artists resources: " + e.getMessage()
              + " (seems that happens sometimes, but the next request is successful afterwards.");
    }
    return new ArtistResourcesImpl(name, Collections.<Image>emptyList());
  }
}
