package callete.api.services.impl.music.network;

import callete.api.services.music.model.Album;
import callete.api.services.music.model.Song;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

/**
 * Used to serialized a single mp3 information.
 */
public class Mp3File extends Song {
  private File file;
  private ID3v1 tag;
  private Mp3Folder folder;
  private com.mpatric.mp3agic.Mp3File mp3file;

  public Mp3File(Mp3Folder folder, File file) {
    super(folder, file);
    this.folder = folder;
    this.file = file;
  }

  private ID3v1 getTag() {
    try {
      if(tag == null) {
        com.mpatric.mp3agic.Mp3File mp3file = getMp3File();
        if(mp3file.hasId3v1Tag()) {
          tag = mp3file.getId3v1Tag();

        }
        else if(mp3file.hasId3v2Tag()) {
          tag = mp3file.getId3v2Tag();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tag;
  }
  
  private com.mpatric.mp3agic.Mp3File getMp3File() {
     if(mp3file == null) {
       try {
         mp3file = new com.mpatric.mp3agic.Mp3File(file);
       } catch (IOException e) {
         e.printStackTrace();
       } catch (UnsupportedTagException e) {
         e.printStackTrace();
       } catch (InvalidDataException e) {
         e.printStackTrace();
       }
     }
    return mp3file;
  } 

  @Override
  public String getName() {
    String name = getTag().getTitle();
    if(name == null) {
      name = file.getName();
    }
    return name;
  }

  public String getArtist() {
    return getTag().getArtist();
  }

  public Album getAlbum() {
    return folder;
  }

  public String getTitle() {
    String title = getTag().getTitle();
    if(title == null) {
      title = file.getName();
    }
    return title;
  }

  @Override
  public int getYear() {
    try {
      String year = getTag().getYear();
      return Integer.parseInt(year);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  @Override
  public long getDurationMillis() {
    return getMp3File().getLengthInMilliseconds();
  }

  public File getFile() {
    return file;
  }

}
