package callete.api.services.music.model;

import org.apache.commons.lang.StringUtils;

/**
 * The model that represents an album.
 */
public class Album extends Playlist {
  private String artist;
  private String genre;
  private int year;
  private String id;

  public Album(String artist, String name) {
    super(name);
    this.artist = artist;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getId() {
    if(id == null) {
      StringBuilder key = new StringBuilder();
      if(!StringUtils.isEmpty(getName())) {
        key.append(getName());
      }
      key.append("-");
      if(!StringUtils.isEmpty(getArtist())) {
        key.append(getArtist());
      }

      id = key.toString();
      id = id.replaceAll(" ", "-");
      id = id.replaceAll("'", "-");
      id = id.replaceAll("%", "-");
      id = id.replaceAll(":", "-");
      id = id.replaceAll("!", "");
      id = id.replaceAll("/", "-");
      id = id.replaceAll("\\\\", "-");
      id = id.replaceAll("\\?", "-");
      id = id.replaceAll("\\&", "-");
      id = id.replaceAll("\\$", "-");
      id = id.replaceAll("\\.", "-");
      id = id.replaceAll("'", "");
      id = id.replaceAll(",", "-");
      id = id.replaceAll("ó", "o");
      id = id.replaceAll("í", "i");
      id = id.replaceAll("á", "ai");
      id = id.replaceAll("é", "e");
      id = id.replaceAll("ö", "oe");
      id = id.replaceAll("ä", "ae");
      id = id.replaceAll("ü", "ue");
      id = id.replaceAll("--", "-");
      id = id.toLowerCase();
    }

    return id;
  }

  @Override
  public String toString() {
    return "Album '" + getName() + "' by '" + artist + "', tracks: " + getSize();
  }
}
