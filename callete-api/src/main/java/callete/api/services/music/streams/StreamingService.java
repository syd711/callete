package callete.api.services.music.streams;

import callete.api.services.Service;
import callete.api.services.music.model.Stream;

import java.util.List;

/**
 * Service interface for providing internet streams for playback.
 */
public interface StreamingService extends Service {

  /**
   * Returns the list of playable streams.
   */
  List<Stream> getStreams();
}
