package callete.samples;

import callete.api.Callete;
import callete.api.services.gpio.*;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.model.Album;

import java.util.List;

/**
 * Example how to use the Google Music API.
 */
public class GoolgeMusicGPIOExample {

  public static void main(String[] args) throws MusicServiceAuthenticationException {
    final GoogleMusicService googleMusicService = Callete.getGoogleMusicService();
    googleMusicService.authenticate();
    List<Album> albums = googleMusicService.getAlbums();
    Album album = albums.get(0);

    System.out.println("Playing " + album + " starting with " + album.getSongs().get(0));
    googleMusicService.playPlaylist(album);

    GPIOService gpioService = Callete.getGPIOService();
    PushButton pushButton = gpioService.connectPushButton(13, "Next Track");
    pushButton.addPushListener(new PushListener() {
      @Override
      public void pushed(PushEvent pushEvent) {
        Callete.getMusicPlayer().next();
      }

      @Override
      public long getPushDebounceMillis() {
        return 700;
      }
    });

  }
}
