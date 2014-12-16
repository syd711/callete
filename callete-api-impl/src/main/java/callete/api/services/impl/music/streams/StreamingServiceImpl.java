package callete.api.services.impl.music.streams;

import callete.api.Callete;
import callete.api.services.music.model.Stream;
import callete.api.services.music.streams.StreamingService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the streaming service.
 */
@SuppressWarnings("unused")
public class StreamingServiceImpl implements StreamingService {
  private final static Logger LOG = LoggerFactory.getLogger(StreamMetaDataProviderImpl.class);
  public static final String STREAMING_PLAYLIST_URL_PROPERTY = "streaming.playlist.url";

  private List<Stream> streams;

  @Override
  public List<Stream> getStreams() {
    if(streams == null) {
      streams = loadStreams();
    }
    return streams;
  }

  //---------------------- Helper -------------------------------------

  private List<Stream> loadStreams() {
    Configuration configuration = Callete.getConfiguration();
    String playlistUrl = configuration.getString(STREAMING_PLAYLIST_URL_PROPERTY);
    if(StringUtils.isEmpty(playlistUrl)) {
      LOG.info("Loading streaming information from properties");
      return loadStreams(configuration);
    }

    LOG.info("Loading streaming information from url " + playlistUrl);
    return loadStreams(playlistUrl);
  }

  /**
   * Loads the playlist from the given playlist url.
   */
  @SuppressWarnings("unchecked")
  private List<Stream> loadStreams(String playlistUrl) {
    try {
      URL url = new URL(playlistUrl);
      Configuration configuration = new PropertiesConfiguration(url);
      return loadStreams(configuration);
    } catch (MalformedURLException e) {
      LOG.error("Invalid URL for loading streaming information: " + e.getMessage(), e);
      return Collections.EMPTY_LIST;
    } catch (ConfigurationException e) {
      LOG.error("Failed to build properties configuration from url " + playlistUrl + ": " + e.getMessage(), e);
      return Collections.EMPTY_LIST;
    }
  }

  /**
   * Loads the streams from a properties file and returns the representing model
   */
  private List<Stream> loadStreams(Configuration properties) {
    List<Stream> streams = new ArrayList<>();

    Iterator<String> keys = properties.getKeys();
    while (keys.hasNext()) {
      String key = keys.next();
      if (!key.contains("stream.") || key.equals(STREAMING_PLAYLIST_URL_PROPERTY)) {
        continue;
      }
      //ignore naming properties
      if (key.endsWith(".name")) {
        continue;
      }

      String url = properties.getString(key);

      String nameKey = key + ".name";
      String name = null;
      if (properties.containsKey(nameKey)) {
        name = properties.getString(nameKey);
      }

      Stream info = new Stream();
      info.setUrl(url);
      info.setName(name);
      streams.add(info);
    }
    return streams;
  }
}
