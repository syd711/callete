package callete.samples;

import callete.api.Callete;
import callete.api.services.music.MusicSearchResult;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.Song;

import java.util.List;

/**
 * Example how to use the Google Music API.
 */
public class GoolgeMusicSearchExample {

  public static void main(String[] args) throws MusicServiceAuthenticationException {
    final GoogleMusicService googleMusicService = Callete.getGoogleMusicService();
    googleMusicService.authenticate();

    String term = "depeche";
    MusicSearchResult result = googleMusicService.search(term);
    List<Album> albums = result.getAlbums();
    System.out.println("Albums found for '" + term + "'");
    System.out.println("-----------------------------------------------");
    for (Album album : albums) {
      System.out.println("\t- " + album);
    }

    List<Album> artists = result.getArtists();
    System.out.println("Artists found for '" + term + "'");
    System.out.println("-----------------------------------------------");
    for (Album album : artists) {
      System.out.println("\t- " + album);
    }

    List<Song> songs = result.getSongs();
    System.out.println("\n\nSongs found for '" + term + "'");
    System.out.println("-----------------------------------------------");
    for (Song song : songs) {
      System.out.println("\t- " + song);
    }

  }
}
