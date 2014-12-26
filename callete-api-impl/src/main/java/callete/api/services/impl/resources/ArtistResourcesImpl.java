package callete.api.services.impl.resources;

import callete.api.services.resources.ArtistResources;
import callete.api.services.resources.ImageResource;
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
import java.util.stream.Collectors;

/**
 * Image resource implementation, used for caching, sizing and cropping.
 */
public class ArtistResourcesImpl implements ArtistResources {
  private final static Logger LOG = LoggerFactory.getLogger(ArtistResourcesImpl.class);
  private List<Image> images = new ArrayList<>();
  private String artist;

  public ArtistResourcesImpl(String artist, List<Image> images) {
    this.images = filter(images);
    this.artist = artist;
  }

  @Override
  public boolean isEmpty() {
    return images.isEmpty();
  }

  @Override
  public String getArtist() {
    return artist;
  }

  @Override
  public ImageResource getRandomImage(int width, int height, int minImageSize) {
    ImageResource randomImage = getRandomImage(minImageSize);
    if (randomImage == null) {
      return null;
    }

    BufferedImage image = randomImage.getImage();
    try {
      if (image != null) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        //e.g. 533x800
        if (width >= height) {
          if (imageWidth > imageHeight) {
            double heightRatio = height / imageHeight;
            double scaleWidth = imageWidth * heightRatio;
            double scaleHeight = imageHeight * heightRatio;
            if (scaleWidth < width) {
              double widthFactor = width / scaleWidth;
              scaleWidth = scaleWidth * widthFactor;
              scaleHeight = scaleHeight * widthFactor;
            }
            image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, (int) scaleWidth + 1, (int) scaleHeight + 1, Scalr.OP_ANTIALIAS);
          }
          else {
            double widthRatio = width / imageWidth;
            double scaleWidth = imageWidth * widthRatio;
            double scaleHeight = imageHeight * widthRatio;
            image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, (int) scaleWidth + 1, (int) scaleHeight + 1, Scalr.OP_ANTIALIAS);
          }

          LOG.info("Scaled image to " + image.getWidth() + "x" + image.getHeight());
        }

        image = Scalr.crop(image, width, height, Scalr.OP_ANTIALIAS, Scalr.OP_DARKER);
        LOG.info("Cropped image to " + image.getWidth() + "x" + image.getHeight());
        return new ImageResourceImpl(randomImage.getUrl(), image);
      }
    } catch (Exception e) {
      LOG.error("Error retrieving image for artist " + artist + ": " + e.getMessage(), e);
    }

    return null;
  }

  @Override
  public ImageResource getRandomImage(int minImageSize) {
    try {
      //we return the first image that matches the size, so lets randomize them
      long seed = System.nanoTime();
      Collections.shuffle(images, new Random(seed));

      //no image size given, so apply first randomized hit
      if(minImageSize <= 0 && !images.isEmpty()) {
        Image image = images.get(0);
        URL imageURL = new URL(image.getURL());
        BufferedImage bufferedImage = ImageIO.read(imageURL);
        return new ImageResourceImpl(image.getURL(), bufferedImage);
      }

      for (Image image : images) {
        URL imageURL = new URL(image.getURL());
        BufferedImage bufferedImage = ImageIO.read(imageURL);
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        if (imageHeight > minImageSize || imageWidth > minImageSize) {
//        if (imageHeight == 1606) {
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

  /**
   * Filters the given images, removes gifs since they are not scalable.
   * @param images the images to filter.
   */
  private List<Image> filter(List<Image> images) {
    return images.stream().filter(i -> !i.getURL().endsWith(".gif")).collect(Collectors.toList());
  }
}
