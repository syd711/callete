package callete.api.services.impl.music.resources;

import callete.api.services.music.resources.ImageResource;
import com.echonest.api.v4.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Image resource implementation, used for caching, sizing and cropping.
 */
public class ImageResourceImpl implements ImageResource {
  private final static int MIN_IMAGE_SIZE = 400;
  private final static Logger LOG = LoggerFactory.getLogger(ImageResourceImpl.class);
  private List<Image> images = new ArrayList<>();
  private String artist;

  public ImageResourceImpl(String artist, List<Image> images) {
    this.images = images;
    this.artist = artist;
  }

  @Override
  public BufferedImage getRandomImage(int width, int height) {
    BufferedImage image = getRandomImage();
    try {
      if (image != null) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        BufferedImage resize;

        if (width >= height) {
          if (imageWidth > imageHeight && imageHeight < height) {
            double heightRatio = height / imageHeight;
            double scaleWidth = imageWidth * heightRatio;
            double scaleHeight = imageHeight * heightRatio;
            resize = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, (int) scaleWidth, (int) scaleHeight, Scalr.OP_ANTIALIAS);
          }
          else {
            double widthRatio = width / imageWidth;
            double scaleWidth = imageWidth * widthRatio;
            double scaleHeight = imageHeight * widthRatio;
            resize = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, (int) scaleWidth, (int) scaleHeight, Scalr.OP_ANTIALIAS);
          }

          LOG.info("Scaled image to " + resize.getWidth() + "x" + resize.getHeight());
          resize = Scalr.crop(resize, width, height, Scalr.OP_ANTIALIAS, Scalr.OP_DARKER);
          return resize;
        }
      }
    } catch (Exception e) {
      LOG.error("Error retrieving image for artist " + artist + ": " + e.getMessage(), e);
    }

    return null;
  }

  @Override
  public javafx.scene.canvas.Canvas getRandomFXImageCanvas(int width, int height) {
    try {
      BufferedImage resize = getRandomImage(width, height);
      if(resize != null) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resize, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        javafx.scene.image.Image image = new javafx.scene.image.Image(is, width, height, false, true);
        ImageView img = new ImageView(image);
        final javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(width, height);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(img.getImage(), 0, 0);
        return canvas;
      }
    } catch (Exception e) {
      LOG.error("Error retrieving FX image: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String toString() {
    return "Image Resources '" + artist + "'";
  }

  // --------------- Helper ----------------------

  private BufferedImage getRandomImage() {
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
          return bufferedImage;
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
}
