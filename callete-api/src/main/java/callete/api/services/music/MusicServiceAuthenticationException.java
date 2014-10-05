package callete.api.services.music;

/**
 * Exception thrown when the authentication to the music provider fails.
 */
public class MusicServiceAuthenticationException extends Exception {
  public MusicServiceAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public MusicServiceAuthenticationException(String s) {
    super(s);
  }
}
