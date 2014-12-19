package callete.api.services.impl.music.resources;

import callete.api.services.music.resources.ImageResource;
import callete.api.services.music.resources.ImageResources;
import com.echonest.api.v4.Image;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Image resource implementation, used for caching, sizing and cropping.
 */
public class ImageResourcesImpl implements ImageResources {
  private final static int MIN_IMAGE_SIZE = 400;
  private final static Logger LOG = LoggerFactory.getLogger(ImageResourcesImpl.class);
  private List<Image> images = new ArrayList<>();
  private String artist;

  public ImageResourcesImpl(String artist, List<Image> images) {
    this.images = images;
    this.artist = artist;
  }

  @Override
  public ImageResource getRandomImage(int width, int height) {
    ImageResource randomImage = getRandomImage();
    if(randomImage == null) {
      return null;
    }

    BufferedImage image = randomImage.getImage();
    try {
      if (image != null) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        BufferedImage resize;

        //e.g. 457x300
        if (width >= height) {
          if (imageWidth > imageHeight && imageHeight < height) {
            double heightRatio = height / imageHeight;
            double scaleWidth = imageWidth * heightRatio;
            double scaleHeight = imageHeight * heightRatio;
            if(scaleWidth < width) {
              double widthFactor = width/scaleWidth;
              scaleWidth = scaleWidth*widthFactor;
              scaleHeight = scaleHeight*widthFactor;
            }
            resize = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, (int) scaleWidth+1, (int) scaleHeight+1, Scalr.OP_ANTIALIAS);
          }
          else {
            double widthRatio = width / imageWidth;
            double scaleWidth = imageWidth * widthRatio;
            double scaleHeight = imageHeight * widthRatio;
            resize = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, (int) scaleWidth+1, (int) scaleHeight+1, Scalr.OP_ANTIALIAS);
          }

          LOG.info("Scaled image to " + resize.getWidth() + "x" + resize.getHeight());
          resize = Scalr.crop(resize, width, height, Scalr.OP_ANTIALIAS, Scalr.OP_DARKER);
          LOG.info("Cropped image to " + resize.getWidth() + "x" + resize.getHeight());
          return new ImageResourceImpl(randomImage.getUrl(), resize);
        }
      }
    } catch (Exception e) {
      LOG.error("Error retrieving image for artist " + artist + ": " + e.getMessage(), e);
    }

    return null;
  }

  @Override
  public ImageResource getRandomImage() {
    try {
      //we return the first image that matches the size, so lets randomize them
      long seed = System.nanoTime();
      Collections.shuffle(images, new Random(seed));

      for (Image image : images) {
        URL imageURL = new URL(image.getURL());
        BufferedImage bufferedImage = ImageIO.read(imageURL);
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        if (imageHeight > MIN_IMAGE_SIZE || imageWidth > MIN_IMAGE_SIZE) {
          LOG.info("Image size match found, resolved " + imageWidth + "x" + imageHeight + " for " + image.getURL());
          return new ImageResourceImpl(image.getURL(), bufferedImage);
        }
        else {
          LOG.info("Ignoring image resource " + image.getURL() + ", cause ratio is only " + imageWidth + "x" + imageHeight);
        }
      }
      LOG.info("No image found for artist '" + artist + "'");
    } catch (Exception e) {
      LOG.error("Error search for a matching image for " + artist + ": " + e.getMessage());
    }
    return null;
  }

  @Override
  public String toString() {
    return "Image Resources '" + artist + "'";
  }
}
