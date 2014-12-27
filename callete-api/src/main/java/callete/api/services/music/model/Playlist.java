package callete.api.services.music.model;

import callete.api.services.ServiceModel;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A collection of songs, identified by a name.
 */
public abstract class Playlist implements ServiceModel {
  private List<Song> songs = new ArrayList<>();

  private String name;
  private String artUrl;

  public Playlist(String name) {
    this.name = name;
  }

  public String getArtUrl() {
    return artUrl;
  }

  public String getName() {
    return name;
  }

  public List<Song> getSongs() {
//    Collections.sort(songs, new Comparator<Song>() {
//      @Override
//      public int compare(Song o1, Song o2) {
//        return o1.compareTo(o2);
//      }
//    });
    return songs;
  }

  public int getSize() {
    return songs.size();
  }

  public void setArtUrl(String artUrl) {
    this.artUrl = artUrl.replaceAll("http:https:https", "http");
  }

  /**
   * Checks if the given song is already part of the playlist.
   */
  public boolean containsSong(Song compare) {
    for(Song song : songs) {
      if(song.getName().toLowerCase().equalsIgnoreCase(compare.getName().toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  public String getFormattedDuration(String format) {
    long durationMillis = 0;
    for(Song songs : getSongs()) {
      durationMillis+= songs.getDurationMillis();
    }
    if(durationMillis > 0) {
      durationMillis-=3600000;
      return DateFormatUtils.format(durationMillis, format);
    }
    return "";
  }

  /**
   * Applies a track id if not set already.
   */
  public void addSong(Song song) {
    if(song.getTrack() == 0) {
      song.setTrack(this.getSize()+1);
    }
    songs.add(song);
  }

  public String getDuration() {
    long duration = 0;
    for(Song song : songs) {
      duration+=song.getDurationMillis();
    }
    if(duration > 0) {
      return DateFormatUtils.format(duration, "mm:ss");
    }
    return "";
  }

  @Override
  public String toString() {
    return "Playlist '" + getName() + "', tracks: " + getSize();
  }
}
