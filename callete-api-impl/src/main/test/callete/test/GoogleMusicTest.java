package callete.test;

import callete.api.Callete;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.Song;
import org.junit.Test;

import java.util.List;

/**
 */
public class GoogleMusicTest {

  @Test
  public void testGoogleLogin() throws MusicServiceAuthenticationException {
    final GoogleMusicService googleMusicService = Callete.getGoogleMusicService();
    googleMusicService.authenticate();

    List<Album> albums = googleMusicService.getAlbums();
    for(Album album : albums) {
      if(album.getName().contains("Violator")) {
        System.out.println(album);
        for(Song song : album.getSongs()) {
          System.out.println("\t" + song);
        }
      }
    }
  }
}
