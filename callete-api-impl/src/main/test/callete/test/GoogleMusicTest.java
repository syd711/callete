package callete.test;

import callete.api.Callete;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.google.GoogleMusicService;
import org.junit.Test;

/**
 */
public class GoogleMusicTest {

  @Test
  public void testGoogleLogin() throws MusicServiceAuthenticationException {
    final GoogleMusicService googleMusicService = Callete.getGoogleMusicService();
    googleMusicService.authenticate();
  }
}
