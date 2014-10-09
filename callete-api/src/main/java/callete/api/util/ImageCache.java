package callete.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
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
public class ImageCache {
  private final static Logger LOG = LoggerFactory.getLogger(ImageCache.class);

  private static final File IMAGE_CACHE_DIR = new File("./image_cache/");
  private static Map<String, File> imageCache = new HashMap<String, File>();

  static {
    final File[] files = IMAGE_CACHE_DIR.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });
    for (File file : files) {
      String id = file.getName().substring(0, file.getName().length() - 4);
      imageCache.put(id.toLowerCase(), file);
    }
  }


//
//  public static File createLazyLoadingImageCanvas(final String url) {
//    try {
//      if (imageCache.containsKey(id.toLowerCase())) {
//        File image = imageCache.get(id.toLowerCase());
//        imageUrl = image.toURI().toURL().toString();
//      }
//      else {
//
//        File target = new File(IMAGE_CACHE_DIR, id + ".png");
//        ImageIO.write(image, "png", target);
//        LOG.info("Written " + target.getAbsolutePath() + " to cache, URL: " + imageUrl);
//        imageCache.put(id, target);
//        imageUrl = target.toURI().toURL().toString();
//
//      }
//    } catch (IOException e) {
//      LOG.error("Error storing image to cache: " + e.getMessage());
//    }
//  }

  /**
   * Creates an AWT image object from the given URL
   * @param imageUrl the URL to create the image from
   */
  private Image load(String imageUrl) throws IOException {
    imageUrl = imageUrl.replaceAll("http:https", "http"); //scale to used size
    LOG.info("Caching " + imageUrl);
    URL imgUrl = new URL(imageUrl);
    return ImageIO.read(imgUrl);
  }
}
