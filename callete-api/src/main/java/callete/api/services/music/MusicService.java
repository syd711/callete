package callete.api.services.music;

import callete.api.services.Service;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.model.Song;

import java.util.List;

/**
 * Interface to be implemented by all music services, like Google, Spotify or local file system support.
 */
public interface MusicService extends Service {

  /**
   * Login to the service to retrieve the users music information.
   */
  void authenticate() throws MusicServiceAuthenticationException;

  /**
   * Returns a list of all albums the user has uploaded.
   */
  List<Album> getAlbums();

  /**
   * Returns a list of all playlist the user has created.
   */
  List<Playlist> getAllPlayLists();

  /**
   * Returns all songs of the providing service.
   */
  List<Song> getAllSongs();

  /**
   * Returns the album for the given id.
   * @param albumId The id that identifies the album, differs for each service.
   * @return The album to look for.
   */
  Album getAlbum(String albumId);

  /**
   * Executes a search for the given search term The search result may contain
   * songs and albums.
   * @param term The search term used for the search.
   */
  MusicSearchResult search(String term);

  /**
   * Playback of a single song.
   * @param item The item to play.
   */
  void play(PlaylistItem item);

  /**
   * Playback of a playlist which can be an album too.
   * @param list playlist to play.
   */
  void playPlaylist(Playlist list);
}
