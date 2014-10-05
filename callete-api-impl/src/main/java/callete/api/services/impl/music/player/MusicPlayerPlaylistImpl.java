package callete.api.services.impl.music.player;

import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.MusicPlayerPlaylist;
import callete.api.services.music.player.PlaylistChangeEvent;
import callete.api.services.music.player.PlaylistChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Playlist implementation for the music player.
 * The playlist implementation of the MPD api is used here and synchronized with the local model.
 * This way it is ensured that always the complete model is returned when methods of the playlist are called.
 */
public class MusicPlayerPlaylistImpl implements MusicPlayerPlaylist {
  //the model used to store the getActiveItem playlist in.
  private List<PlaylistItem> playlist = new ArrayList<>();

  //event listeners that listen on playlist changes
  private List<PlaylistChangeListener> changeListeners = new ArrayList<>();

  // the getActiveItem playback index, used to skip forward and backward.
  private int playbackIndex = 0;

  @Override
  public PlaylistItem getActiveItem() {
    if (!playlist.isEmpty()) {
      return playlist.get(playbackIndex);
    }
    return null;
  }

  @Override
  public void addItem(PlaylistItem item) {
    firePlaylistChangedEvent();
    this.playlist.add(item);
  }

  @Override
  public void addPlaylist(Playlist playlist) {
    firePlaylistChangedEvent();
    this.playlist.addAll(playlist.getSongs());
  }

  @Override
  public void addItems(List<? extends PlaylistItem> items) {
    firePlaylistChangedEvent();
    this.playlist.addAll(items);
  }

  @Override
  public void clear() {
    firePlaylistChangedEvent();
    this.playlist.clear();
  }

  @Override
  public PlaylistItem next() {
    playbackIndex++;
    if(playlist.size() > playbackIndex) {
      return playlist.get(playbackIndex);
    }
    //reset index to the last position
    playbackIndex--;
    return null;
  }

  @Override
  public PlaylistItem previous() {
    playbackIndex--;
    if(playbackIndex >= 0 && playbackIndex < playlist.size()) {
      return playlist.get(playbackIndex);
    }
    playbackIndex++;
    return null;
  }

  @Override
  public void addChangeListener(PlaylistChangeListener listener) {
    changeListeners.add(listener);
  }

  @Override
  public List<PlaylistItem> getPlaylistItems() {
    return playlist;
  }

  // ---------------------- Helper ----------------------------

  private void firePlaylistChangedEvent() {
    PlaylistChangeEvent e = new PlaylistChangedEventImpl();
    for(PlaylistChangeListener l : changeListeners) {
      l.playlistChanged(e);
    }
  }
}
