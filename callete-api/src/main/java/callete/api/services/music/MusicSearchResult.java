package callete.api.services.music;

import callete.api.services.music.model.Album;
import callete.api.services.music.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Search result wrapper for searches executed for a music service.
 */
public class MusicSearchResult {
  private List<Album> albums = new ArrayList<>();
  private List<Album> artists = new ArrayList<>();
  private List<Song> songs = new ArrayList<>();

  public int getHits() {
    return albums.size() + songs.size();
  }

  public List<Album> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Album> albums) {
    this.albums = albums;
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }

  public List<Album> getArtists() {
    return artists;
  }

  public void setArtists(List<Album> artists) {
    this.artists = artists;
  }
}
