package callete.api.services.music.model;

import callete.api.services.music.PlaybackUrlProvider;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * The model that represents a song.
 */
public class Song implements PlaylistItem, Comparable<Song> {
  private String id;
  private String name;
  private int year;
  private String artist;
  private String genre;
  private String albumName;
  private String albumArtUrl;
  private long durationMillis;
  private float creationDate;
  private int track;
  private Album album;
  private String composer;

  private Object originalModel;

  private byte[] artwork;
  private PlaybackUrlProvider urlProvider;

  public Song(PlaybackUrlProvider urlProvider) {
    this.urlProvider = urlProvider;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlbumArtUrl() {
    return albumArtUrl;
  }

  public void setAlbumArtUrl(String albumArtUrl) {
    this.albumArtUrl = albumArtUrl;
  }

  @Override
  public String toString() {
    return "'" + name + "' by " + artist;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public float getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(float creationDate) {
    this.creationDate = creationDate;
  }

  public long getDurationMillis() {
    return durationMillis;
  }

  public void setDurationMillis(long durationMillis) {
    this.durationMillis = durationMillis;
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public Object getOriginalModel() {
    return originalModel;
  }

  public void setOriginalModel(Object originalModel) {
    this.originalModel = originalModel;
  }

  public int getTrack() {
    return track;
  }

  public void setTrack(int track) {
    this.track = track;
  }

  public String getComposer() {
    return composer;
  }

  public void setComposer(String composer) {
    this.composer = composer;
  }

  public String getDuration() {
    if(this.durationMillis > 0) {
      return DateFormatUtils.format(this.durationMillis, "mm:ss");
    }
    return "";
  }

  @Override
  public int compareTo(Song o) {
    if(o.getTrack() == 0 || this.getTrack() == 0) {
      return getName().compareTo(o.getName());
    }
    return track - o.getTrack();
  }

  public byte[] getArtwork() {
    return artwork;
  }

  public void setArtwork(byte[] artwork) {
    this.artwork = artwork;
  }

  @Override
  public String getPlaybackUrl() {
    return urlProvider.provideUrl(originalModel);
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public Album getAlbum() {
    return album;
  }
}
