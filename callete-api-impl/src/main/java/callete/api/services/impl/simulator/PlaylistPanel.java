package callete.api.services.impl.simulator;

import callete.api.Callete;
import callete.api.services.impl.music.player.PlaybackChangeEventImpl;
import callete.api.services.impl.music.player.PlaylistChangedEventImpl;
import callete.api.services.music.model.PlaylistItem;
import callete.api.services.music.player.PlaybackChangeEvent;
import callete.api.services.music.player.PlaybackChangeEventListener;
import callete.api.services.music.player.PlaylistChangeEvent;
import callete.api.services.music.player.PlaylistChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implements the playlist panel with all action and event listener.
 */
public class PlaylistPanel extends JPanel implements PlaylistChangeListener, PlaybackChangeEventListener {
  private DefaultListModel<PlaylistItem> playlist;
  private JList<PlaylistItem> playlistList;

  public PlaylistPanel() {
    super(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder("Playlist"));
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    JButton prevButton = new JButton("<");
    actionsPanel.add(prevButton);
    prevButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Callete.getMusicPlayer().previous();
      }
    });
    JButton stopButton = new JButton("[X]");
    actionsPanel.add(stopButton);
    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Callete.getMusicPlayer().stop();
      }
    });
    JButton playButton = new JButton("|>");
    actionsPanel.add(playButton);
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Callete.getMusicPlayer().play();
      }
    });
    JButton pauseButton = new JButton("||");
    actionsPanel.add(pauseButton);
    pauseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Callete.getMusicPlayer().pause();
      }
    });
    JButton nextButton = new JButton(">");
    actionsPanel.add(nextButton);
    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Callete.getMusicPlayer().next();
      }
    });

    Callete.getMusicPlayer().getPlaylist().addChangeListener(this);
    Callete.getMusicPlayer().addPlaybackChangeEventListener(this);

    add(actionsPanel, BorderLayout.NORTH);

    playlist = new DefaultListModel<>();
    playlistList = new JList<>(playlist);
    JScrollPane listScroller = new JScrollPane(playlistList);
    playlistList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    add(listScroller, BorderLayout.CENTER);

    playlistChanged(new PlaylistChangedEventImpl());
    playbackChanged(new PlaybackChangeEventImpl());
  }


  @Override
  public void playlistChanged(PlaylistChangeEvent e) {
    playlist.removeAllElements();
    java.util.List<PlaylistItem> playlistItems = e.getPlaylistItems();
    for(PlaylistItem item : playlistItems) {
      playlist.addElement(item);
    }
  }

  @Override
  public void playbackChanged(PlaybackChangeEvent event) {
    int index = event.getActiveIndex();
    playlistList.setSelectedIndex(index);
  }
}
