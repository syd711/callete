package callete.test;

import callete.api.services.impl.music.streams.StreamMetaDataProviderImpl;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.assertTrue;

/**
 */
public class StreamMetaDataProviderTest {

  @Test
  public void testInvalidUrl() throws MalformedURLException {
    String url = "http://live96.106acht.de";
    StreamMetaDataProviderImpl metaDataProvider = new StreamMetaDataProviderImpl(new URL(url), null);
    assertTrue(metaDataProvider.getArtist().equals(""));
  }

  @Test
  public void testValidUrl() throws MalformedURLException {
    String url = "http://webradio.antennevorarlberg.at:80/classicrock";
    StreamMetaDataProviderImpl metaDataProvider = new StreamMetaDataProviderImpl(new URL(url), null);
    System.out.println("Name: " + metaDataProvider.getName());
    System.out.println("Title: " + metaDataProvider.getTitle());
    System.out.println("Artist: " + metaDataProvider.getArtist());
  }
}
