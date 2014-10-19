package callete.api.services.weather;

import callete.api.services.Service;

import java.util.List;

/**
 * The weather service provides several weather information
 * and a default weather that should be displayed by default.
 */
public interface WeatherService extends Service {

  /**
   * The default refresh interval, 5 min.
   */
  public final static long DEFAULT_REFRESH_INTERVAL = 1000*60*5;

  /**
   * Returns a list of all weather information to retrieve data for.
   */
  List<Weather> getWeather();

  /**
   * Returns the weather information for the given position in the properties file.
   * @param pos the weather id to return, e.g. "2" for value "weather.2"
   */
  Weather getWeatherAt(int pos);

  /**
   * Sets the update interval for the weather retrieval
   * @param millis the millis to wait until the service data is refreshed.
   */
  void setRefreshInterval(long millis);
}
