package callete.api.services.music.model;

import callete.api.services.music.streams.StreamMetaDataProvider;

import java.io.IOException;

public class Stream implements PlaylistItem {
  private StreamMetaDataProvider metaDataProvider;

  public Stream(StreamMetaDataProvider metaDataProvider) {
    this.metaDataProvider = metaDataProvider;
  }

  /**
   * Get artist using stream's title
   */
  public String getArtist() throws IOException {
    return metaDataProvider.getArtist();
  }

  /**
   * Get title using stream's title
   */
  public String getTitle() throws IOException {
    return metaDataProvider.getArtist();
  }

  @Override
  public String getPlaybackUrl() {
    return metaDataProvider.getStreamUrl();
  }

  @Override
  public String toString() {
    return "Stream '" + getPlaybackUrl() + "'";
  }
}