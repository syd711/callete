package callete.api.services.impl.music.google;

import callete.api.services.music.MusicSearchResult;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.AlbumCollection;
import callete.api.services.music.model.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of the Google Music Service, using jkiddo's API which can be found here:
 * https://github.com/jkiddo/gmusic.api
 */
@SuppressWarnings("unused")
public class GoogleMusicServiceImpl implements GoogleMusicService {
  private final static Logger LOG = LoggerFactory.getLogger(JkiddoGoogleApi.class);
  private JkiddoGoogleApi api = new JkiddoGoogleApi();

  @Override
  public boolean authenticate() throws MusicServiceAuthenticationException {
    try {
      api.connect();
      return true;
    } catch (MusicServiceAuthenticationException e) {
      LOG.error("Error during authentication: " + e.getMessage());
      return false;
    }
  }

  @Override
  public List<Album> getAlbums() {
    return api.getAlbums();
  }

  @Override
  public List<Song> getAllSongs() {
    return api.getAllSongs();
  }

  @Override
  public Album getAlbum(String albumId) {
    return api.getAlbum(albumId);
  }

  @Override
  public MusicSearchResult search(String term) {
    return api.search(term);
  }

  @Override
  public List<AlbumCollection> getAlbumByNameLetter() {
    return api.getAlbumByLetter();
  }

  @Override
  public List<AlbumCollection> getAlbumsByArtistLetter() {
    return api.getArtistByLetter();
  }
}
