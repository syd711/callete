package callete.api.services.music.model;

import callete.api.services.ServiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a selection of albums, e.g. sorted by name or artists.
 */
public class AlbumCollection implements ServiceModel {
  private List<Album> albums = new ArrayList<>();
  private String letter;

  public AlbumCollection(String letter) {
    this.letter = letter;
  }

  public List<Album> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Album> albums) {
    this.albums = albums;
  }

  public String getLetter() {
    return letter;
  }
}
