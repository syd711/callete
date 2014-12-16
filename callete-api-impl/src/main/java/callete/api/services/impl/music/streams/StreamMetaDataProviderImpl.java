package callete.api.services.impl.music.streams;

import callete.api.services.music.streams.StreamMetaDataProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stream Provider implementation using the streams metadata to fill
 * the stream model.
 */
public class StreamMetaDataProviderImpl implements StreamMetaDataProvider {
  private final static Logger LOG = LoggerFactory.getLogger(StreamMetaDataProviderImpl.class);
  public static final String STREAM_ARTIST = "StreamArtist";
  public static final String STREAM_TITLE = "StreamTitle";
  public static final String STREAM_NAME = "StreamName";
  private Map<String, String> metadata;
  private URL url;
  private boolean available = true;
  private String name;

  public StreamMetaDataProviderImpl(URL url, String name) {
    this.url = url;
    this.name = name;
  }

  @Override
  public String getArtist() {
    Map<String, String> data = getMetadata();

    if (data == null || !data.containsKey(STREAM_ARTIST)) {
      return "";
    }

    return data.get(STREAM_ARTIST);
  }

  @Override
  public String getName() {
    if (!StringUtils.isEmpty(name)) {
      return name;
    }
    Map<String, String> data = getMetadata();
    if (data == null || !data.containsKey(STREAM_NAME)) {
      return "";
    }

    return data.get(STREAM_NAME);
  }

  @Override
  public String getTitle() {
    Map<String, String> data = getMetadata();

    if (data == null || !data.containsKey(STREAM_TITLE)) {
      return "";
    }

    return data.get(STREAM_TITLE);
  }

  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public String getStreamUrl() {
    return url.toString();
  }

  // ----------------------- Helper -----------------------------------

  /**
   * Lazy loading of the the stream's meta data.
   */
  private Map<String, String> getMetadata() {
    if (metadata == null && available) {
      retreiveMetadata();
    }

    return metadata;
  }

  /**
   * Parses the meta data string and puts all values into a map.
   */
  private Map<String, String> parseMetadata(String metaString) {
    Map<String, String> metadata = new HashMap<>();
    String[] metaParts = metaString.split(";");
    for(String part : metaParts) {
      if(part.contains("=")) {
        String[] items = part.split("=");
        metadata.put(items[0], items[1]);
      }
    }

    String streamTitle = metadata.get(STREAM_TITLE);
    if(!StringUtils.isEmpty(streamTitle)) {
      if(streamTitle.startsWith("'")) {
        streamTitle = streamTitle.substring(1, streamTitle.length());
      }
      if(streamTitle.endsWith("'")) {
        streamTitle = streamTitle.substring(0, streamTitle.length()-1);
      }

      if(streamTitle.contains("-")) {
        String artist = streamTitle.substring(0, streamTitle.indexOf("-"));
        metadata.put(STREAM_ARTIST, artist.trim());
        String title = streamTitle.substring(streamTitle.indexOf("-")+1, streamTitle.length());
        metadata.put(STREAM_TITLE, title.trim());
      }
      else {
        metadata.put(STREAM_NAME, streamTitle);
      }
    }

    return metadata;
  }

  private void retreiveMetadata() {
    try {
      URLConnection con = url.openConnection();
      con.setRequestProperty("Icy-MetaData", "1");
      con.setRequestProperty("Connection", "close");
      con.setRequestProperty("Accept", null);
      con.connect();

      int metaDataOffset = 0;
      Map<String, List<String>> headers = con.getHeaderFields();
      InputStream stream = con.getInputStream();

      if (headers.containsKey("icy-metaint")) {
        // Headers are sent via HTTP
        metaDataOffset = Integer.parseInt(headers.get("icy-metaint").get(0));
      } else {
        // Headers are sent within a stream
        StringBuilder strHeaders = new StringBuilder();
        char c;
        while ((c = (char)stream.read()) != -1) {
          strHeaders.append(c);
          if (strHeaders.length() > 5 && (strHeaders.substring((strHeaders.length() - 4), strHeaders.length()).equals("\r\n\r\n"))) {
            // end of headers
            break;
          }
          //TODO quickfix
          if(strHeaders.length() > 5) {
            break;
          }
        }

        // Match headers to get metadata offset within a stream
        Pattern p = Pattern.compile("\\r\\n(icy-metaint):\\s*(.*)\\r\\n");
        Matcher m = p.matcher(strHeaders.toString());
        if (m.find()) {
          metaDataOffset = Integer.parseInt(m.group(2));
        }
      }

      // In case no data was sent
      if (metaDataOffset == 0) {
        available = false;
        return;
      }

      // Read metadata
      int b;
      int count = 0;
      int metaDataLength = 4080; // 4080 is the max length
      boolean inData;
      StringBuilder metaData = new StringBuilder();
      // Stream position should be either at the beginning or right after headers
      while ((b = stream.read()) != -1) {
        count++;

        // Length of the metadata
        if (count == metaDataOffset + 1) {
          metaDataLength = b * 16;
        }

        inData = count > metaDataOffset + 1 && count < (metaDataOffset + metaDataLength);
        if (inData) {
          if (b != 0) {
            metaData.append((char)b);
          }
        }
        if (count > (metaDataOffset + metaDataLength)) {
          break;
        }

      }

      // Set the data
      metadata = parseMetadata(metaData.toString());

      // Close
      stream.close();
    } catch (IOException e) {
      available = false;
      LOG.error("Error retrieving meta data for stream " + getStreamUrl() + ": " + e.getMessage());
    }
  }
}
