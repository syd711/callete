package callete.test;

import callete.api.Callete;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.model.Album;
import callete.api.services.music.network.NetworkMusicService;
import org.junit.Test;

import java.util.List;

/**
 */
public class NetworkMusicTest {

  @Test
  public void test() throws MusicServiceAuthenticationException {
    NetworkMusicService networkMusicService = Callete.getNetworkMusicService();
    networkMusicService.authenticate();


    List<Album> albums = networkMusicService.getAlbums();
    for(Album album : albums) {
      System.out.println(album);
    }
  }
}
