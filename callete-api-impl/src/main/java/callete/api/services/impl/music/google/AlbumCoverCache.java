package callete.api.services.impl.music.google;

import callete.api.services.music.model.Album;
import callete.api.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Caching of music cover images
 */
public class AlbumCoverCache {
  private final static Logger LOG = LoggerFactory.getLogger(AlbumCoverCache.class);

  private static File IMAGE_CACHE_DIR = new File("./image_cache/");
  public static final String PNG = "png";
  private static Map<String, File> imageCache;

  public static void setCacheDir(File cacheDir) {
    LOG.info("Setting album cover cache dir to " + cacheDir.getAbsolutePath());
    IMAGE_CACHE_DIR = cacheDir;
  }

  private static void initializeCache() {
    LOG.info("Initializing album cover cache, using lookup directory " + IMAGE_CACHE_DIR.getAbsolutePath());
    imageCache = new HashMap<>();
    if(!IMAGE_CACHE_DIR.exists() && !IMAGE_CACHE_DIR.mkdirs()) {
      LOG.warn("Failed to create image cache dir " + IMAGE_CACHE_DIR.getAbsolutePath());
    }

    if(!IMAGE_CACHE_DIR.exists()) {
      String path = new File("./").getAbsolutePath();
      List<String> cmds = Arrays.asList("sudo", "mkdir", "image_cache");
      SystemUtils.executeSystemCommand(path, cmds);
      cmds = Arrays.asList("sudo", "chmod", "777", "image_cache");
      SystemUtils.executeSystemCommand(path, cmds);
    }

    if(IMAGE_CACHE_DIR.exists()) {
      final File[] files = IMAGE_CACHE_DIR.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".png");
        }
      });
      for(File file : files) {
        String id = file.getName().substring(0, file.getName().length() - 4);
        imageCache.put(id, file);
      }
      LOG.info("Cache initialization finished: found " + imageCache.size() + " cached images");
    }
    else {
      LOG.error("Failed to initialize image cache, caching directory " + IMAGE_CACHE_DIR.getAbsolutePath() + " does not exist.");
    }
  }


  public static String loadCover(final Album album) {
    return loadCached(album.getId(), album.getArtUrl());
  }

  public static String loadCached(String id, String imageUrl) {
    if(imageCache == null) {
      initializeCache();
    }

    try {
      if(imageCache.containsKey(id)) {
        File image = imageCache.get(id);
        imageUrl = image.toURI().toURL().toString();
      }
      else if(imageUrl == null) {
        return null;
      }
      else {
        if(imageUrl.startsWith("http")) {
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
        else if(imageUrl.startsWith("file://")) {
          String filename = imageUrl.substring("file://".length(), imageUrl.length());
          BufferedImage image = ImageIO.read(new File(filename));
          File target = new File(IMAGE_CACHE_DIR, id + "." + PNG);
          ImageIO.write(image, PNG, target);
          LOG.info("Written " + target.getAbsolutePath() + " to cache, URL: " + imageUrl);
          imageCache.put(id, target);
          imageUrl = target.toURI().toURL().toString();
        }

      }
    } catch (IOException e) {
      LOG.error("Error storing image to cache: " + e.getMessage());
    }
    return imageUrl;
  }
}
