package callete.api.services.impl.resources;

import callete.api.services.resources.ImageResource;

import java.awt.image.BufferedImage;

/**
 */
public class ImageResourceImpl implements ImageResource {
  private String url;
  private BufferedImage image;

  public ImageResourceImpl(String url, BufferedImage image) {
    this.url = url;
    this.image = image;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public String getImageFormat() {
    if (url.endsWith(".png")) {
      return "png";
    }
    return "jpg";
  }

  @Override
  public BufferedImage getImage() {
    return image;
  }
}
