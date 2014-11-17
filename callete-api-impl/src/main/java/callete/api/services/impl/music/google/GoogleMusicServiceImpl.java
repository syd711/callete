package callete.api.services.impl.music.google;

import callete.api.services.music.MusicSearchResult;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.MusicServiceImpl;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.Song;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the Google Music Service, using jkiddo's API which can be found here:
 * https://github.com/jkiddo/gmusic.api
 */
@SuppressWarnings("unused")
public class GoogleMusicServiceImpl extends MusicServiceImpl implements GoogleMusicService {
  private JkiddoGoogleApi api = new JkiddoGoogleApi();

  @Override
  public void authenticate() throws MusicServiceAuthenticationException {
    api.connect();
  }

  @Override
  public List<Album> getAlbums() {
    return api.getAlbums();
  }

  @Override
  public List<Playlist> getAllPlayLists() {
    return api.getAllPlaylists();
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
  public Map<String, List<Album>> getAlbumByNameLetter() {
    return api.getAlbumByLetter();
  }

  @Override
  public Map<String, List<Album>> getAlbumsByArtistLetter() {
    return api.getArtistByLetter();
  }
}
