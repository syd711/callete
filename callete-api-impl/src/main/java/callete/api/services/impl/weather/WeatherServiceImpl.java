package callete.api.services.impl.weather;

import callete.api.services.impl.RefreshingService;
import callete.api.services.weather.Weather;
import callete.api.services.weather.WeatherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for retrieving the weather information.
 * The yahoo weather api is used here to read the weather location
 * from the corresponding RSS streams.
 */
@SuppressWarnings("unused")
public class WeatherServiceImpl extends RefreshingService implements WeatherService {

  private YahooWeather yahooWeather = new YahooWeather();

  private Map<Integer, Weather> cachedWeather = Collections.emptyMap();

  public WeatherServiceImpl() {
    super(WeatherService.DEFAULT_REFRESH_INTERVAL);
  }

  @Override
  public Weather getWeatherAt(int pos) {
    if(cachedWeather.isEmpty()) {
      forceRefresh();
    }
    return cachedWeather.get(pos);
  }

  @Override
  public List<Weather> getWeather() {
    if(cachedWeather.isEmpty()) {
      forceRefresh();
    }
    return new ArrayList<>(cachedWeather.values());
  }

  @Override
  protected void refreshServiceData() {
    cachedWeather = yahooWeather.getWeather();
  }

  @Override
  public void setRefreshInterval(long millis) {
    super.setRefreshInterval(millis);
  }
}
