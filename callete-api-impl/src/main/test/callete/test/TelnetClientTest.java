package callete.test;

import callete.api.services.impl.music.player.MPDMetaDataFactory;
import callete.api.services.impl.music.player.MPDTelnetClient;
import callete.api.services.music.player.PlaylistMetaData;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 */
public class TelnetClientTest {

  @Test
  public void testTelnetClient() {
    MPDTelnetClient client = new MPDTelnetClient("192.168.1.43", 6600);
    client.connect();


    String info = client.playlistInfo();
    PlaylistMetaData metaData = MPDMetaDataFactory.createMetaData(null, info);
    assertTrue(metaData.getTitle() != null);
    assertTrue(metaData.getName() != null);
    assertTrue(metaData.getArtist() != null);
  }
}
