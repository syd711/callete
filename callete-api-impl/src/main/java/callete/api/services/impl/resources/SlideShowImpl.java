package callete.api.services.impl.resources;

import callete.api.services.resources.SlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class SlideShowImpl implements SlideShow {
  private final static Logger LOG = LoggerFactory.getLogger(SlideShowImpl.class);

  private List<BufferedImage> images = new ArrayList<>();

  public SlideShowImpl(File directory) {
    loadSlideShowImages(directory);
  }

  @Override
  public List<BufferedImage> getImages() {
    return images;
  }

  /**
   * Load buffered images
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
        try {
          BufferedImage bufferedImage = ImageIO.read(file);
          images.add(bufferedImage);
        } catch (IOException e) {
          LOG.error("Error reading slide show file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
        }
      }
    }
    else {
      LOG.error("Failed to create slide show, directory " + directory.getAbsolutePath() + " does not exist.");
    }
  }
}
