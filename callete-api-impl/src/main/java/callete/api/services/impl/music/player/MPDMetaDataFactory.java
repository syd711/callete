package callete.api.services.impl.music.player;

import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.model.Stream;
import callete.api.services.music.player.PlaylistMetaData;
import org.apache.commons.lang.StringUtils;
import org.bff.javampd.objects.MPDSong;

/**
 * Meta data factory to retrieve meta data from a MPD client.
 */
public class MPDMetaDataFactory {

  /**
   * The meta is read from the MPD client, using the current song information.
   * Additional formatting based on the expected artist/title format is done.
   */
  public static PlaylistMetaData createMetaData(PlaylistItem item, MPDSong song) {
    if (item != null && item instanceof Stream) {
      String title = song.getTitle();
      String artist = song.getArtistName();

      if (StringUtils.isEmpty(artist) && !StringUtils.isEmpty(title)) {
        if (title.startsWith("'") || title.startsWith("\"")) {
          title = title.substring(1, title.length());
        }
        if (title.endsWith("'") || title.endsWith("\"")) {
          title = title.substring(0, title.length() - 1);
        }

        if (title.contains("-")) {
          artist = title.substring(0, title.indexOf("-")).trim();
          title = title.substring(title.indexOf("-") + 1, title.length()).trim();
        }

        //some german stations using a word as separator
        if (title.contains(" von ")) {
          artist = title.substring(0, title.indexOf(" von ")+1).trim();
          title = title.substring(title.indexOf("von") + "von".length()+1, title.length()).trim();
        }

        if(title.contains("|")) {
          title = title.substring(0, title.indexOf("|"));
        }
      }
      return new PlaylistMetaData(item, artist, title);
    }
    return null;
  }
}
