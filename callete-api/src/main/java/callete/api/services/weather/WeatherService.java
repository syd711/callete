package callete.api.services.weather;

import callete.api.services.Service;

import java.util.List;

/**
 * The weather service provides several weather information
 * and a default weather that should be displayed by default.
 */
public interface WeatherService extends Service {

  /**
   * Returns a list of all weather information to retrieve data for.
   */
  List<Weather> getWeather();

}
