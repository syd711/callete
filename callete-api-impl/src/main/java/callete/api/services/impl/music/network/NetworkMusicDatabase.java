package callete.api.services.impl.music.network;

import callete.api.Callete;
import callete.api.services.music.PlaybackUrlProvider;
import callete.api.services.music.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * Well, not a real database, but cached results.
 */
public class NetworkMusicDatabase  implements PlaybackUrlProvider {
  private final static Logger LOG = LoggerFactory.getLogger(NetworkMusicDatabase.class);
  private final static String MUSIC_DIRECTORY = "music.directory";

  private Map<String, Song> songs = new HashMap<>();
  private Map<String, Album> albums = new HashMap<>();
  private Map<String, List<Album>> artists = new HashMap<>();
  private List<AlbumCollection> artistByLetter = new ArrayList<>();
  private List<AlbumCollection> albumByLetter = new ArrayList<>();

  private String musicDir; 

  @Override
  public String provideUrl(Object originalSongModel) {
    String musicDir = Callete.getConfiguration().getString(MUSIC_DIRECTORY);
    String path = ((File)originalSongModel).getPath();
    path = path.substring(musicDir.length(), path.length());
    if(path.startsWith("/")) {
      path = path.substring(1, path.length());
    }
    return path;
  }


  public void init() {    
    musicDir = Callete.getConfiguration().getString(MUSIC_DIRECTORY);
    LOG.info("Initializing music database with folder " + musicDir);
        
    if(musicDir == null) {
      String msg = "No music folder provided in the collete settings, ensure that " +
          "the property " + MUSIC_DIRECTORY+ " points to a valid directory";
      LOG.info(msg);
      throw new UnsupportedOperationException(msg);
    }
    LOG.info("Scanning folder " + musicDir);
    scan(null, new File(musicDir));

    LOG.info("Scanned " + albums.size() + " folders");

    for(Album album : albums.values()) {
      String artist = album.getArtist();
      if(!StringUtils.isEmpty(artist)) {
        //fill albums by artist
        if(!artists.containsKey(artist)) {
          List<Album> artistAlbums = new ArrayList<>();
          artists.put(artist, artistAlbums);
        }
        List<Album> artAlbums = artists.get(artist);
        artAlbums.add(album);

        //fill albums by artist letter
        String startingLetter = artist.substring(0, 1).toUpperCase();
        AlbumCollection collection = getCollection(startingLetter, artistByLetter);
        collection.getAlbums().add(album);
      }

      //fill albums by album letter
      String albumName = album.getName();
      if(!StringUtils.isEmpty(albumName)) {
        String startingLetter = album.getName().substring(0, 1).toUpperCase();
        AlbumCollection collection = getCollection(startingLetter, albumByLetter);
        collection.getAlbums().add(album);
      }
    }

    sortResults();
  }
  
  /**
   * Recursive search for mp3 files.
   *
   * @param folder the current lookup folder.
   */
  private void scan(Mp3Folder parent, File folder) {
    LOG.info("Scanning " + folder.getAbsolutePath());
    try {
      Mp3Folder mp3Folder = new Mp3Folder(parent, folder);
      scanMp3(mp3Folder);

      File[] subFolders = folder.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return new File(dir, name).isDirectory();
        }
      });


      for(File subFolder : subFolders) {
        scan(mp3Folder, subFolder);
      }
    } catch (Exception e) {
      LOG.error("Error scanning folder " + folder.getAbsolutePath() + ": " + e.getMessage(), e);
    }
  }

  private void scanMp3(Mp3Folder folder) {
    try {
      File[] listFiles = folder.getFolder().listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith(".ogg");
        }
      });

      for(File file : listFiles) {
        Mp3File mp3File = new Mp3File(this, folder, file);
        songs.put(mp3File.getFile().getName().toLowerCase(), mp3File);
        folder.addSong(mp3File);
      }

      //check for cover art
      File[] coverArt = folder.getFolder().listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png");
        }
      });

      if(coverArt.length > 0){
        folder.setArtUrl("file://" + coverArt[0].getAbsolutePath());
      }

      if(!folder.getSongs().isEmpty()) {
        albums.put(folder.getAlbumName().toLowerCase(), folder);
      }
    } catch (Exception e) {
      LOG.error("Error scanning mp3s in folder " + folder.getFolder().getAbsolutePath() + ": " + e.getMessage(), e);
    }
  }


  /**
   * Sorts all collection and the end of the dictionary creation.
   */
  private void sortResults() {
    Collections.sort(albumByLetter, new AlbumCollectionComparator());
    Collections.sort(artistByLetter, new AlbumCollectionComparator());

    for(AlbumCollection collection : albumByLetter) {
      Collections.sort(collection.getAlbums(), new AlbumNameComparator());
    }

    for(AlbumCollection collection : artistByLetter) {
      Collections.sort(collection.getAlbums(), new AlbumArtistComparator());
    }
  }

  private AlbumCollection getCollection(String letter, List<AlbumCollection> collections) {
    for(AlbumCollection collection : collections) {
      if(collection.getLetter().equals(letter)) {
        return collection;
      }
    }

    AlbumCollection collection = new AlbumCollection(letter);
    collections.add(collection);
    return collection;
  }

  public List<Album> getAlbums() {
    return new ArrayList(albums.values());
  }

  public List<Song> getSongs() {
    return new ArrayList(songs.values());
  }

  public List<AlbumCollection> getAlbumByNameLetter() {
    return albumByLetter;
  }

  public List<AlbumCollection> getAlbumsByArtistLetter() {
    return artistByLetter;
  }
}
