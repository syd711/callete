package callete.api.services.music;

import callete.api.Callete;
import callete.api.services.music.model.Playlist;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.MusicPlayerService;

/**
 * Implementation of some default methods of the music service.
 */
abstract public class MusicServiceImpl implements MusicService {

  @Override
  public void play(PlaylistItem item) {
    MusicPlayerService player = Callete.getMusicPlayer();
    player.getPlaylist().clear();
    player.getPlaylist().addItem(item);
    player.play();
  }

  @Override
  public void playPlaylist(Playlist list) {
    MusicPlayerService player = Callete.getMusicPlayer();
    player.getPlaylist().clear();
    player.getPlaylist().addPlaylist(list);
    player.play();
  }
}
