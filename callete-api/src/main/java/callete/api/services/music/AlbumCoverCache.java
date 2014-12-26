package callete.api.services.music;

import callete.api.services.music.model.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Caching of music cover images
 */
public class AlbumCoverCache {
  private final static Logger LOG = LoggerFactory.getLogger(AlbumCoverCache.class);

  private static final File IMAGE_CACHE_DIR = new File("./image_cache/");
  public static final String PNG = "png";
  private static Map<String, File> imageCache = new HashMap<>();

  static {
    IMAGE_CACHE_DIR.mkdirs();

    final File[] files = IMAGE_CACHE_DIR.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });
    for (File file : files) {
      String id = file.getName().substring(0, file.getName().length() - 4);
      imageCache.put(id, file);
    }
    LOG.info("Cache initialization finished: found " + imageCache.size() + " cached images");
  }


  public static String loadCover(final Album album) {
    return loadCached(album.getId(), album.getArtUrl());
  }

  public static String loadCached(String id, String imageUrl) {
    try {
      if (imageCache.containsKey(id)) {
        File image = imageCache.get(id);
        imageUrl = image.toURI().toURL().toString();
      }
      else
      {
        imageUrl = imageUrl.replaceAll("http:https", "http"); //scale to used size
        LOG.info("Caching " + imageUrl);
        URL imgUrl = new URL(imageUrl);
        BufferedImage image = ImageIO.read(imgUrl);
        File target = new File(IMAGE_CACHE_DIR, id + "." + PNG);
        ImageIO.write(image, PNG, target);
        LOG.info("Written " + target.getAbsolutePath() + " to cache, URL: " + imageUrl);
        imageCache.put(id, target);
        imageUrl = target.toURI().toURL().toString();

      }
    } catch (IOException e) {
      LOG.error("Error storing image to cache: " + e.getMessage());
    }
    return imageUrl;
  }
}
