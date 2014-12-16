package callete.api.services.music;

import callete.api.services.Service;
import callete.api.services.music.model.*;

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
   * Returns all albums sort by the starting letter.
   * The letter is stored upper case in the map as key.
   * @return all albums accessable by their starting letter
   */
  List<AlbumCollection> getAlbumByNameLetter();

  /**
   * Returns all albums sort by the artist's starting letter.
   * The letter is stored upper case in the map as key.
   * @return all albums accessable by their artist's starting letter
   */
  List<AlbumCollection> getAlbumsByArtistLetter();
}
