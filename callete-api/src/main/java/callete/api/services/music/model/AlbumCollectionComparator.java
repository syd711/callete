package callete.api.services.music.model;

import java.util.Comparator;

/**
 * Supports different sort directions for Albums.
 */
public class AlbumCollectionComparator implements Comparator<AlbumCollection> {

  @Override
  public int compare(AlbumCollection o1, AlbumCollection o2) {
    return o1.getLetter().compareTo(o2.getLetter());
  }
}
