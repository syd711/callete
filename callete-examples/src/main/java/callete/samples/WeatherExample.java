package callete.samples;

import callete.api.Callete;
import callete.api.services.weather.Weather;
import callete.api.services.weather.WeatherService;

/**
 * Simple gaia service examples that prints the weather information.
 */
public class WeatherExample {

  public static void main(String[] args) {
    WeatherService weatherService = Callete.getWeatherService();
    for (Weather weather : weatherService.getWeather()) {
      System.out.println(weather);
    }
    System.exit(0);
  }
}
