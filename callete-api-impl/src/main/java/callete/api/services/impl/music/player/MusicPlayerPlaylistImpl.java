package callete.api.services.impl.music.player;

import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Playlist implementation for the music player.
 * The playlist implementation of the MPD api is used here and synchronized with the local model.
 * This way it is ensured that always the complete model is returned when methods of the playlist are called.
 */
public class MusicPlayerPlaylistImpl implements MusicPlayerPlaylist {
  private List<PlaylistMetaDataChangeListener> metaDataChangeListeners = new ArrayList<>();

  //the model used to store the getActiveItem playlist in.
  private List<PlaylistItem> playlist = new ArrayList<>();

  //event listeners that listen on playlist changes
  private List<PlaylistChangeListener> changeListeners = new ArrayList<>();

  // the getActiveItem playback index, used to skip forward and backward.
  private int playbackIndex = 0;

  @Override
  public void updateMetaData(PlaylistMetaData metaData) {
    for(PlaylistMetaDataChangeListener listener : new ArrayList<>(metaDataChangeListeners)) {
      listener.updateMetaData(metaData);
    }
  }

  @Override
  public PlaylistItem getActiveItem() {
    if(!playlist.isEmpty()) {
      return playlist.get(playbackIndex);
    }
    return null;
  }

  @Override
  public void setPlaylist(Playlist playlist) {
    clear();
    this.playlist.addAll(playlist.getSongs());
    firePlaylistChangedEvent();
  }

  @Override
  public void setActiveItem(PlaylistItem item) {
    if(playlist.contains(item)) {
      playbackIndex = playlist.indexOf(item);
    } else {
      playbackIndex = 0;
      playlist.clear();
      playlist.add(item);
    }
    firePlaylistChangedEvent();
  }

  @Override
  public void clear() {
    playbackIndex = 0;
    this.playlist.clear();
  }

  @Override
  public PlaylistItem next() {
    playbackIndex++;
    if(playlist.size() > playbackIndex) {
      firePlaylistChangedEvent();
      return playlist.get(playbackIndex);
    }
    //reset index to the last position
    playbackIndex--;
    firePlaylistChangedEvent();
    return null;
  }

  @Override
  public PlaylistItem previous() {
    playbackIndex--;
    if(playbackIndex >= 0 && playbackIndex < playlist.size()) {
      firePlaylistChangedEvent();
      return playlist.get(playbackIndex);
    }
    playbackIndex++;
    firePlaylistChangedEvent();
    return null;
  }

  @Override
  public void addChangeListener(PlaylistChangeListener listener) {
    changeListeners.add(listener);
  }

  @Override
  public void removeChangeListener(PlaylistChangeListener listener) {
    changeListeners.remove(listener);
  }

  @Override
  public void addMetaDataChangeListener(PlaylistMetaDataChangeListener listener) {
    this.metaDataChangeListeners.add(listener);
  }

  @Override
  public void removeMetaDataChangeListener(PlaylistMetaDataChangeListener listener) {
    this.metaDataChangeListeners.remove(listener);
  }

  @Override
  public List<PlaylistItem> getPlaylistItems() {
    return playlist;
  }

  // ---------------------- Helper ----------------------------

  private void firePlaylistChangedEvent() {
    PlaylistChangeEvent e = new PlaylistChangedEventImpl(playlist, getActiveItem());
    for(PlaylistChangeListener l : changeListeners) {
      l.playlistChanged(e);
    }
  }
}
