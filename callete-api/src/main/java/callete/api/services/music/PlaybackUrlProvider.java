package callete.api.services.music;

/**
 * Playing songs via web, like Google Music requires to create playback URLs.
 * Since Google doesn't want to have fix download URLs for songs, these ones have
 * to be creates on the fly. That means that when the next song is played from the getActiveItem
 * playlist, the playback URL has to be created first.
 * <p/>
 * So every music service has to implement a PlaybackUrlProvider so that the music player
 * implementation, e.g. mpd, doesn't have to care about that and simple retrieves the URL from the song itself.
 */
public interface PlaybackUrlProvider {

  /**
   * Returns the playback URL for the given song. Since the song itself contains the original model
   * from which it has been created, the song can pass the original model to the PlaybackUrlProvider.
   *
   * @param originalSongModel the original song model
   * @return The url that can be used to playback the song.
   */
  String provideUrl(Object originalSongModel);
}
