package callete.api.services.music.player;

import callete.api.services.Service;

/**
 * Service interface to be implemented for a player.
 * System players like the mplayer or mpd can be used for this.
 */
public interface MusicPlayerService extends Service {

  /**
   * Starts the playback of the getActiveItem playlist.
   * Resumes the playback if the player is in paused mode.
   */
  void play();

  /**
   * Stops the playback of the getActiveItem playlist, but keeps the playlist.
   */
  void stop();

  /**
   * Pauses the playback.
   */
  void pause();

  /**
   * Plays the next song of the playlist.
   * Returns true if there is a next song in the playlist.
   */
  boolean next();

  /**
   * Plays the previous song of the playlist.
   * Returns true if there is previous song in the playlist.
   */
  boolean previous();

  /**
   * Returns the playlist of the music player.
   */
  MusicPlayerPlaylist getPlaylist();

  /**
   * Registers a new listener to detect playback changes.
   * @param listener the listener to register.
   */
  void addPlaybackChangeEventListener(PlaybackChangeListener listener);

  /**
   * Removes the listener from the player.
   * @param listener the listener to remove.
   */
  void removePlaybackChangeEventListener(PlaybackChangeListener listener);
}
