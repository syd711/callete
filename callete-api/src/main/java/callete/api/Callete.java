package callete.api;

import callete.api.services.ServiceFactory;
import callete.api.services.gpio.GPIOService;
import callete.api.services.http.HttpService;
import callete.api.services.music.google.GoogleMusicService;
import callete.api.services.music.player.MusicPlayerService;
import callete.api.services.music.streams.StreamingService;
import callete.api.services.network.NetworkService;
import callete.api.services.resources.ResourcesService;
import callete.api.services.system.SystemService;
import callete.api.services.template.TemplateService;
import callete.api.services.weather.WeatherService;
import callete.api.util.Config;
import callete.api.util.Settings;
import org.apache.commons.configuration.Configuration;

import java.io.File;

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
  private static ResourcesService resourcesService;
  private static SystemService systemService;
  private static NetworkService networkService;
  private static TemplateService templateService;
  

  public static MusicPlayerService getMusicPlayer() {
    if(musicPlayer == null) {
      musicPlayer = (MusicPlayerService) ServiceFactory.createService(MusicPlayerService.class);
    }
    return musicPlayer;
  }

  public static WeatherService getWeatherService() {
    if(weatherService == null) {
      weatherService = (WeatherService) ServiceFactory.createService(WeatherService.class);
    }
    return weatherService;
  }

  public static GPIOService getGPIOService() {
    if(gpioService == null) {
      gpioService = (GPIOService) ServiceFactory.createService(GPIOService.class);
    }
    return gpioService;
  }

  public static HttpService getHttpService() {
    if(httpService == null) {
      httpService = (HttpService) ServiceFactory.createService(HttpService.class);
    }
    return httpService;
  }

  public static GoogleMusicService getGoogleMusicService() {
    if(googleMusicService == null) {
      googleMusicService = (GoogleMusicService) ServiceFactory.createService(GoogleMusicService.class);
    }
    return googleMusicService;
  }

  public static StreamingService getStreamingService() {
    if(streamingService == null) {
      streamingService = (StreamingService) ServiceFactory.createService(StreamingService.class);
    }
    return streamingService;
  }

  public static ResourcesService getResourcesService() {
    if(resourcesService == null) {
      resourcesService = (ResourcesService) ServiceFactory.createService(ResourcesService.class);
    }
    return resourcesService;
  }

  public static SystemService getSystemService() {
    if(systemService == null) {
      systemService = (SystemService) ServiceFactory.createService(SystemService.class);
    }
    return systemService;
  }

  public static TemplateService getTemplateService() {
    if(templateService== null) {
      templateService = (TemplateService) ServiceFactory.createService(TemplateService.class);
    }
    return templateService;
  }

  public static NetworkService getNetworkService() {
    if(networkService== null) {
      networkService = (NetworkService) ServiceFactory.createService(NetworkService.class);
    }
    return networkService;
  }

  public static Configuration getSettings() {
    return Settings.getSettings();
  }

  public static void saveSetting(String key, Object value) {
    Settings.saveSetting(key, value);
  }
}
