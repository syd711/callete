package callete.api.services.impl.resources;

import callete.api.services.resources.ImageResource;
import callete.api.services.resources.SlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 */
public class SlideShowImpl implements SlideShow {
  private final static Logger LOG = LoggerFactory.getLogger(SlideShowImpl.class);

  private List<ImageResource> images = new ArrayList<>();
  private Iterator<ImageResource> iterator;
  private boolean randomized = false;
  private File directory;

  public SlideShowImpl(File directory, boolean randomized) {
    this.directory = directory;
    this.randomized = randomized;
    loadSlideShowImages(directory);
  }

  @Override
  public int size() {
    return images.size();
  }

  @Override
  public ImageResource nextImage() {
    if(images.isEmpty()) {
      return null;
    }

    if(iterator == null || !iterator.hasNext()) {
      if(randomized) {
        long seed = System.nanoTime();
        Collections.shuffle(images, new Random(seed));
      }
      LOG.info("Rebuilding slide show iterator for " + this);
      iterator = images.iterator();
    }

    return iterator.next();
  }

  /**
   * Load buffered images
   *
   * @param directory the directory to load the images from.
   */
  private void loadSlideShowImages(File directory) {
    if(directory.exists()) {
      File[] files = directory.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg");
        }
      });
      for(File file : files) {
        ImageResource image = createImageResource(file);
        images.add(image);
      }
    } else {
      LOG.error("Failed to create slide show, directory " + directory.getAbsolutePath() + " does not exist.");
    }
  }

  private ImageResource createImageResource(File file) {
    try {
      LOG.info("Loading image resource " + file.getAbsolutePath());
      BufferedImage bufferedImage = ImageIO.read(file);
      return new ImageResourceImpl(file.getAbsolutePath(), bufferedImage);
    } catch (IOException e) {
      LOG.error("Error reading slide show file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String toString() {
    return "Slide Show for " + directory.getAbsolutePath();
  }
}
