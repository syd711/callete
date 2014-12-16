package callete.api.services.music.streams;

/**
 * Implementing classes retrieve the meta data information
 * about a stream, such as artist, station name, track name...
 */
public interface StreamMetaDataProvider {

  /**
   * Returns the name of the artist that is currently played.
   */
  String getArtist();

  /**
   * Returns the name of the stream.
   */
  String getName();


  /**
   * Returns the name of the stream.
   */
  String getTitle();

  /**
   * Returns the URL the stream is streamed from.
   */
  String getStreamUrl();

  /**
   * Returns false in the case that the stream is not playable.
   */
  boolean isAvailable();
}
