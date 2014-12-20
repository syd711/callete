package callete.api.services.impl.music.resources;

import callete.api.services.music.resources.ArtistResources;
import callete.api.services.music.resources.ArtistResourcesService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the ArtistResourceService interface.
 * We are using the Echo Nest API here, ensure that the developer key
 * is provided in the configuration properties.
 */
@SuppressWarnings("unused")
public class ArtistResourceServiceImpl implements ArtistResourcesService {
  private final static Logger LOG = LoggerFactory.getLogger(ArtistResourceServiceImpl.class);

  private ArtistResourcesLoader cacheLoader;
  private LoadingCache<String, ArtistResources> cache;

  public ArtistResourceServiceImpl() {
    super();
    cacheLoader = new ArtistResourcesLoader();
    cache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(10, TimeUnit.MINUTES).build(cacheLoader);
  }

  @Override
  public ArtistResources getImageResourcesFor(String name) {
    try {
      return cache.get(name);
    } catch (ExecutionException e) {
      LOG.error("Error loading artist resources from cache: " + e.getMessage(), e);
    }
    return null;
  }
}
