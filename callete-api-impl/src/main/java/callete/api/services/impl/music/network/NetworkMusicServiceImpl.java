package callete.api.services.impl.music.network;

import callete.api.services.music.MusicSearchResult;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.AlbumCollection;
import callete.api.services.music.model.Song;
import callete.api.services.music.network.NetworkMusicService;

import java.util.List;

/**
 *
 */
public class NetworkMusicServiceImpl implements NetworkMusicService{
  
  private NetworkMusicDatabase db;
  
  @Override
  public boolean authenticate() throws MusicServiceAuthenticationException {
    db = new NetworkMusicDatabase();
    db.init();
    return true;
  }

  @Override
  public List<Album> getAlbums() {
    return db.getAlbums();
  }

  @Override
  public List<Song> getAllSongs() {
    return db.getSongs();
  }

  @Override
  public Album getAlbum(String albumId) {
    List<Album> albums = getAlbums();
    for(Album album : albums) {
      if(album.getId().equals(albumId)) {
        return album;
      }
    }
    return null;
  }

  @Override
  public MusicSearchResult search(String term) {
    return new MusicSearchResult();
  }

  @Override
  public List<AlbumCollection> getAlbumByNameLetter() {
    return db.getAlbumByNameLetter();
  }

  @Override
  public List<AlbumCollection> getAlbumsByArtistLetter() {
    return db.getAlbumsByArtistLetter();
  }
}
