package callete.api.services.music.resources;

import java.awt.image.BufferedImage;

/**
 * Interface to be implemented by services that provide artist image data.
 */
public interface ImageResource {
  BufferedImage getRandomImage(int width, int height);

  javafx.scene.canvas.Canvas getRandomFXImageCanvas(int width, int height);
}
