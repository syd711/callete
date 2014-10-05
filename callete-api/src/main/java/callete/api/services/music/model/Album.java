package callete.api.services.music.model;

/**
 * The model that represents an album.
 */
public class Album extends Playlist {
  private String artist;
  private String genre;
  private int year;

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

  @Override
  public String toString() {
    return "Album '" + getName() + "' by '" + artist + "', tracks: " + getSize();
  }
}
