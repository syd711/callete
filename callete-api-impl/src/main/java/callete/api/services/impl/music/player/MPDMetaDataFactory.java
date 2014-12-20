package callete.api.services.impl.music.player;

import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.PlaylistMetaData;
import org.apache.commons.lang.StringUtils;

/**
 * Meta data factory to retrieve meta data from a MPD client.
 */
public class MPDMetaDataFactory {

  /**
   * The meta is read from the MPD client, using the current song information.
   * Additional formatting based on the expected artist/title format is done.
   *
   * Status format is, e.g.:
   * <code>
   *   file: http://mp3channels.webradio.rockantenne.de/classic-perlen
   *   Title: Def Leppard - Animal
   *   Name: ROCK ANTENNE Classic Perlen
   *   Pos: 0
   *   Id: 3
   *   OK
   * </code>
   */
  public static PlaylistMetaData createMetaData(PlaylistItem item, String mpdStatus) {
    String[] split = mpdStatus.split("\n");
    if(split.length < 3) {
      return null;
    }

    String name = formatMpdInfo(split[2]);
    String artist = formatMpdInfo(split[1]);
    String title = formatMpdInfo(split[1]);

    if (!StringUtils.isEmpty(title)) {
      if (title.startsWith("'") || title.startsWith("\"")) {
        title = title.substring(1, title.length());
      }
      if (title.endsWith("'") || title.endsWith("\"")) {
        title = title.substring(0, title.length() - 1);
      }

      if (title.contains("-")) {
        artist = title.substring(0, title.lastIndexOf("-")).trim();
        title = title.substring(title.lastIndexOf("-") + 1, title.length()).trim();
      }

      //some german stations using a word as separator
      if (title.contains(" von ")) {
        artist = title.substring(title.indexOf("von") + "von".length()+1, title.length()).trim();
        title = title.substring(0, title.indexOf(" von ")+1).trim();
      }

      if(title.contains("|")) {
        title = title.substring(0, title.indexOf("|"));
      }

      if (artist.startsWith("'") || artist.startsWith("\"")) {
        artist = artist.substring(1, artist.length());
      }
      if (artist.endsWith("'") || artist.endsWith("\"")) {
        artist = artist.substring(0, artist.length() - 1);
      }
    }
    return new PlaylistMetaData(item, name, artist, title);
  }

  private static String formatMpdInfo(String s) {
    return s.substring(s.indexOf(":")+1, s.length()).trim();
  }
}
