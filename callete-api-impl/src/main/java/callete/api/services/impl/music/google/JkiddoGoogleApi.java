package callete.api.services.impl.music.google;

import callete.api.Callete;
import callete.api.services.music.MusicSearchResult;
import callete.api.services.music.MusicServiceAuthenticationException;
import callete.api.services.music.PlaybackUrlProvider;
import callete.api.services.music.model.Album;
import callete.api.services.music.model.AlbumComparator;
import callete.api.services.music.model.Playlist;
import gmusic.api.impl.GoogleMusicAPI;
import gmusic.api.impl.InvalidCredentialsException;
import gmusic.api.model.Song;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Google service delegate using jkiddo's google music api.
 */
public class JkiddoGoogleApi implements PlaybackUrlProvider {
  private final static Logger LOG = LoggerFactory.getLogger(JkiddoGoogleApi.class);
  private GoogleMusicAPI api;

  private Map<String, callete.api.services.music.model.Song> songs = new HashMap<>();
  private Map<String, Album> albums = new HashMap<>();
  private Map<String, List<Album>> artists = new HashMap<>();
  private Map<String, Playlist> playlists = new HashMap<>();
  private Map<String, List<Album>> artistByLetter = new HashMap<>();
  private Map<String, List<Album>> albumByLetter = new HashMap<>();

  /**
   * Logs into google to create the client.
   * The credentials for this have to be provided in the project's .properties file.
   */
  public void connect() throws MusicServiceAuthenticationException {
    api = new GoogleMusicAPI();

    String email = Callete.getConfiguration().getString("google.music.email");
    String password = Callete.getConfiguration().getString("google.music.password");

    if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
      throw new MusicServiceAuthenticationException("Could not login to google music, no email/password provided.");
    }

    try {
      api.login(email, password);
      LOG.info("Login successful, creating music dictionary...");
      createDictionary();
    } catch (IOException e) {
      LOG.error("Error connecting to google music: " + e.getMessage(), e);
      throw new MusicServiceAuthenticationException("Error connecting to google music: " + e.getMessage(), e);
    } catch (URISyntaxException e) {
      LOG.error("Error creating REST url for google music: " + e.getMessage(), e);
      throw new MusicServiceAuthenticationException("Error creating REST url for google music: " + e.getMessage(), e);
    } catch (InvalidCredentialsException e) {
      LOG.error("Invalid email/password provided for google music: " + e.getMessage(), e);
      throw new MusicServiceAuthenticationException("Invalid email/password provided for google music: " + e.getMessage(), e);
    } catch (Exception e) {
      LOG.error("Error creating google music dictionary: " + e.getMessage(), e);
      throw new MusicServiceAuthenticationException("Error creating google music dictionary: " + e.getMessage(), e);
    }
  }

  public List<Playlist> getAllPlaylists() {
    return new ArrayList<>(playlists.values());
  }

  public List<callete.api.services.music.model.Song> getAllSongs() {
    return new ArrayList<>(songs.values());
  }


  public Album getAlbum(String albumId) {
    return albums.get(albumId);
  }

  public List<Album> getAlbums() {
    ArrayList<Album> albumCopy = new ArrayList<>(albums.values());
    Collections.sort(albumCopy, new AlbumComparator());
    return albumCopy;
  }

  public Map<String, List<Album>> getArtistByLetter() {
    return artistByLetter;
  }

  public Map<String, List<Album>> getAlbumByLetter() {
    return albumByLetter;
  }

  public MusicSearchResult search(String term) {
    MusicSearchResult result = new MusicSearchResult();
    try {
      //the actual API search doesn't seem to work, so we search manually.
      for (callete.api.services.music.model.Song next : songs.values()) {
        if (next.getName().toLowerCase().contains(term.toLowerCase())) {
          result.getSongs().add(next);
        }
      }

      Set<Map.Entry<String, Album>> albumSet = albums.entrySet();
      for (Map.Entry<String, Album> entry : albumSet) {
        if (entry.getKey().contains(term.toLowerCase())) {
          result.getAlbums().add(entry.getValue());
        }
      }

      Set<Map.Entry<String, List<Album>>> entries = artists.entrySet();
      for (Map.Entry<String, List<Album>> entry : entries) {
        if (entry.getKey().toLowerCase().contains(term.toLowerCase())) {
          result.getArtists().addAll(entry.getValue());
        }
      }
    } catch (Exception e) {
      LOG.error("Error executing Google Music search: " + e.getMessage(), e);
    }
    return result;
  }

  @Override
  public String provideUrl(Object originalSongModel) {
    try {
      Song originalSong = (Song) originalSongModel;
      String url = api.getSongURL(originalSong).toString();
      url = url.replaceAll("https", "http");
      LOG.info("Resolved playback URL " + url);
      return url;
    } catch (Exception e) {
      LOG.error("Failed to retrieve playback URL for " + originalSongModel + ": " + e.getMessage(), e);
    }
    return null;
  }

  // ------------------------- Helper --------------------------------------
  private void createDictionary() throws Exception {
    try {
      Collection<Song> songs = api.getAllSongs();
      LOG.info(this + " finished loading songs: " + songs.size() + " total");
      for (gmusic.api.model.Song song : songs) {
        callete.api.services.music.model.Song fxSong = songFor(song);
        addSong(fxSong);
      }

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
          String startingLetter = artist.substring(0,1).toUpperCase();
          if(!artistByLetter.containsKey(startingLetter)) {
            List<Album> artistAlbums = new ArrayList<>();
            artistByLetter.put(startingLetter, artistAlbums);
          }
          List<Album> albumsByLetter = artistByLetter.get(startingLetter);
          albumsByLetter.add(album);
        }

        //fill albums by album letter
        String albumName = album.getName();
        if(!StringUtils.isEmpty(albumName)) {
          String startingLetter = album.getName().substring(0,1).toUpperCase();
          if(!albumByLetter.containsKey(startingLetter)) {
            List<Album> albums = new ArrayList<>();
            albumByLetter.put(startingLetter, albums);
          }
          List<Album> albumsByLetter = albumByLetter.get(startingLetter);
          albumsByLetter.add(album);
        }

      }
      LOG.info("Music dictionary creation finished: created " + albums.size() + " albums.");
    } catch (RuntimeException re) {
      LOG.error("Error initializing google music: " + re.getMessage(), re);
      throw re;
    }
  }

  /**
   * Puts all music data from the google song api into the
   * gaia song model.
   *
   * @param song The song to convert.
   * @return The converted song.
   */
  private callete.api.services.music.model.Song songFor(gmusic.api.model.Song song) {
    callete.api.services.music.model.Song apiSong = new callete.api.services.music.model.Song(this);
    apiSong.setOriginalModel(song);
    apiSong.setId(song.getId());

    apiSong.setName(song.getName());

    if (!StringUtils.isEmpty(song.getAlbumArtUrl())) {
      apiSong.setAlbumArtUrl("http:" + song.getAlbumArtUrl());
    }

    apiSong.setAlbum(song.getAlbum());
    apiSong.setArtist(song.getAlbumArtist());
    if (StringUtils.isEmpty(song.getAlbumArtist()) && !StringUtils.isEmpty(song.getArtist())) {
      apiSong.setArtist(song.getArtist());
    }
    apiSong.setComposer(song.getComposer());
    apiSong.setTrack(song.getTrack());
    apiSong.setDurationMillis(song.getDurationMillis());
    apiSong.setYear(song.getYear());
    apiSong.setGenre(song.getGenre());
    return apiSong;
  }

  /**
   * Adds a song to the dictionary, updates all sub-dictionaries afterwards.
   *
   * @param song The song to add.
   */
  private void addSong(callete.api.services.music.model.Song song) {
    songs.put(song.getId(), song);
    addToAlbum(song);
  }

  /**
   * Creates an album for the song if it does not exist yet.
   *
   * @param song The song to add to the album.
   */
  private void addToAlbum(callete.api.services.music.model.Song song) {
    if (!StringUtils.isEmpty(song.getAlbum())) {
      //create the regular dict entry and add song to album
      Album album = getAlbum(song);
      if (!album.containsSong(song)) {
        album.getSongs().add(song);
      }
    }
  }


  /**
   * Returns the album for given dict, creates a new one if it
   * does not exist yet.
   *
   * @param song The song to find the album for.
   */
  private Album getAlbum(callete.api.services.music.model.Song song) {
    Album album;
    if (!albums.containsKey(song.getAlbum())) {
      album = new Album(song.getArtist(), song.getAlbum());
      albums.put(song.getAlbum(), album);
    } else {
      album = albums.get(song.getAlbum());
    }

    if (!StringUtils.isEmpty(song.getAlbumArtUrl()) && StringUtils.isEmpty(album.getArtUrl())) {
      album.setArtUrl(song.getAlbumArtUrl());
    }
    if (StringUtils.isEmpty(album.getArtist()) && !StringUtils.isEmpty(song.getArtist())) {
      album.setArtist(song.getArtist());
    }
    if (StringUtils.isEmpty(album.getGenre()) && !StringUtils.isEmpty(song.getGenre())) {
      album.setGenre(song.getGenre());
    }

    if (song.getYear() > 0) {
      album.setYear(song.getYear());
    }
    return album;
  }

  @Override
  public String toString() {
    return "jkiddo's gmusic api impl";
  }
}
