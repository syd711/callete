package callete.api.services.music.model;

import java.util.Comparator;

/**
 * Supports different sort directions for Albums.
 */
public class AlbumArtistComparator implements Comparator<Album> {

  @Override
  public int compare(Album o1, Album o2) {
    return o1.getArtist().compareTo(o2.getArtist());
  }
}
