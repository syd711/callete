package callete.api.services.music.streams;

import callete.api.services.Service;
import callete.api.services.music.model.Stream;

import java.io.File;
import java.util.List;

/**
 * Service interface for providing internet streams for playback.
 */
public interface StreamingService extends Service {

  /**
   * Returns the list of playable streams.
   */
  List<Stream> getStreams();

  /**
   * Instead of reading the stream configuration from the callete.properties file,
   * an alternative properties file can be applied here.
   * @param configFile the config file to read/write the stream infos
   */
  void setConfigFile(File configFile);

  /**
   * Writes the given streams in the config file using the list's order.
   * @param streams  the streams to persist
   */
  void saveStreams(List<Stream> streams);
}
