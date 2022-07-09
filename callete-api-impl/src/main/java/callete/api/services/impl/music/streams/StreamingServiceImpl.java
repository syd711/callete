package callete.api.services.impl.music.streams;

import callete.api.Callete;
import callete.api.services.music.model.Stream;
import callete.api.services.music.streams.StreamingService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the streaming service.
 */
@SuppressWarnings("unused")
public class StreamingServiceImpl implements StreamingService {
  private final static Logger LOG = LoggerFactory.getLogger(StreamMetaDataProviderImpl.class);

  private List<Stream> streams;
  private File streamConfig;
  private PropertiesConfiguration alternativeConfiguration;

  @Override
  public List<Stream> getStreams() {
    try {
      if(streams == null) {
        PropertiesConfiguration configuration = (PropertiesConfiguration) Callete.getConfiguration();
        if(streamConfig != null) {
          LOG.info("Loading stream from " + streamConfig.getAbsolutePath());
          alternativeConfiguration = new PropertiesConfiguration(streamConfig);
          alternativeConfiguration.load();
          configuration = alternativeConfiguration;
        }
        streams = loadStreams(configuration);
      }
      return streams;
    }
    catch (Exception e) {
      LOG.error("Error loading streams configuration: " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public void setConfigFile(File configFile) {
    this.streamConfig = configFile;
  }

  @Override
  public void saveStreams(List<Stream> streams) {
    if(streamConfig != null) {
      alternativeConfiguration.clear();
      for(int i=1; i<=streams.size(); i++) {
        alternativeConfiguration.setProperty("stream." + i, streams.get(i-1).getPlaybackUrl());
      }

      try {
        alternativeConfiguration.save(streamConfig);
      } catch (ConfigurationException e) {
        LOG.error("Error updating " + streamConfig.getAbsolutePath() + ": " + e.getMessage(), e);
      }
      //reset stream to force reload
      this.streams = null;
    }
    else {
      LOG.error("Trying to save stream properties, but no alternative config file set.");
    }
  }

  //---------------------- Helper -------------------------------------

  /**
   * Loads the streams from a properties file and returns the representing model
   */
  private List<Stream> loadStreams(Configuration properties) {
    List<Stream> streams = new ArrayList<>();

    int index = 1;
    while(true) {
      String key = "stream." + index;
      if(!properties.containsKey(key)) {
        break;
      }
      if(!key.contains("stream.")) {
        continue;
      }
      
      String url = properties.getString(key);

      String nameKey = key + ".name";
      String name;
      if(properties.containsKey(nameKey)) {
        name = properties.getString(nameKey);
      }
      else {
        name = key;
      }

      LOG.info("Creating Stream " + url);
      Stream info = new Stream();
      info.setUrl(url);
      info.setId(index);
      info.setName(name);
      streams.add(info);

      index++;
    }
    return streams;
  }
}
