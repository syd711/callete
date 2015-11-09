package callete.api.services.impl.music.network;

import callete.api.services.music.model.Album;
import callete.api.services.music.model.Song;

import java.io.File;
import java.util.List;

/**
 *
 */
public class Mp3Folder extends Album {

  private File folder;
  private Mp3Folder parent;


  public Mp3Folder(Mp3Folder parent, File folder) {
    super(folder.getName(), folder.getName());
    if(parent != null) {
      setArtist(parent.getName());
    }
    this.folder = folder;
    this.parent = parent;
  }

  public String getName() {
    return folder.getName();
  }

  public String getAlbumName() {
    return folder.getName();
  }

  public File getFolder() {
    return folder;
  }

  @Override
  public int getYear() {
    List<Song> songs = getSongs();
    for(Song song : songs) {
      if(song.getYear() > 0) {
        return song.getYear();
      }
    }
    return -1;
  }

  public Mp3Folder getParent() {
    return parent;
  }
}
