package callete.api;

import callete.api.services.ServiceFactory;
import callete.api.services.gpio.GPIOService;
import callete.api.services.http.HttpService;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.player.MusicPlayerService;
import callete.api.services.music.resources.ArtistResourcesService;
import callete.api.services.music.streams.StreamingService;
import callete.api.services.weather.WeatherService;
import callete.api.util.Config;
import org.apache.commons.configuration.Configuration;

/**
 * Central API service entry point.
 */
public class Callete {
  private final static Configuration config = Config.getConfiguration();

  public static Configuration getConfiguration() {
    return config;
  }

  private static MusicPlayerService musicPlayer;
  private static WeatherService weatherService;
  private static GPIOService gpioService;
  private static HttpService httpService;
  private static GoogleMusicService googleMusicService;
  private static StreamingService streamingService;
  private static ArtistResourcesService artistResourcesService;

  public static MusicPlayerService getMusicPlayer() {
    if (musicPlayer == null) {
      musicPlayer = (MusicPlayerService) ServiceFactory.createService(MusicPlayerService.class);
    }
    return musicPlayer;
  }

  public static WeatherService getWeatherService() {
    if (weatherService == null) {
      weatherService = (WeatherService) ServiceFactory.createService(WeatherService.class);
    }
    return weatherService;
  }

  public static GPIOService getGPIOService() {
    if (gpioService == null) {
      gpioService = (GPIOService) ServiceFactory.createService(GPIOService.class);
    }
    return gpioService;
  }

  public static HttpService getHttpService() {
    if (httpService == null) {
      httpService = (HttpService) ServiceFactory.createService(HttpService.class);
    }
    return httpService;
  }

  public static GoogleMusicService getGoogleMusicService() {
    if (googleMusicService == null) {
      googleMusicService = (GoogleMusicService) ServiceFactory.createService(GoogleMusicService.class);
    }
    return googleMusicService;
  }

  public static StreamingService getStreamingService() {
    if (streamingService == null) {
      streamingService = (StreamingService) ServiceFactory.createService(StreamingService.class);
    }
    return streamingService;
  }

  public static ArtistResourcesService getArtistResourcesService() {
    if (artistResourcesService == null) {
      artistResourcesService = (ArtistResourcesService) ServiceFactory.createService(ArtistResourcesService.class);
    }
    return artistResourcesService;
  }
}
