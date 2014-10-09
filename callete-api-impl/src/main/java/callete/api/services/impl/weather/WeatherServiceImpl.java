package callete.api.services.impl.weather;

import callete.api.services.weather.Weather;
import callete.api.services.weather.WeatherService;

import java.util.List;

/**
 * Service implementation for retrieving the weather information.
 * The yahoo weather api is used here to read the weather location
 * from the corresponding RSS streams.
 */
public class WeatherServiceImpl implements WeatherService {

  private YahooWeather yahooWeather = new YahooWeather();

  @Override
  public Weather getWeatherAt(int pos) {
    return yahooWeather.getWeatherAt(pos);
  }

  @Override
  public List<Weather> getWeather() {
    return yahooWeather.getWeather();
  }
}
