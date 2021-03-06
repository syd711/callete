package callete.api.services.weather;

import callete.api.services.ServiceModel;

import java.util.Date;
import java.util.List;

/**
 * Interface for the common weather data to be initialized.
 */
public interface Weather extends ServiceModel {


  public String getCity();

  public void setCity(String city);

  public String getLowTemp();

  public void setLowTemp(String lowTemp);

  public String getHighTemp();

  public void setHighTemp(String highTemp);

  public WeatherState getWeatherState();

  public void setWeatherState(WeatherState state);

  public Date getForecastDate();

  public void setForecastDate(Date forecastDate);

  public String getCountry();

  public void setCountry(String country);

  public List<Weather> getForecast();

  public void setForecast(List<Weather> forecast);

  public String getLatitude();

  public void setLatitude(String latitude);

  public String getLongitude();

  public void setLongitude(String longitude);

  public Date getLocalTime();

  public void setLocalTime(Date localTime);

  public String getDescription();

  public void setDescription(String description);

  public String getTemp();

  public void setTemp(String temp);

  public double getWind();

  public void setWind(double wind);

  public Date getSunset();

  public void setSunset(Date sunset);

  public Date getSunrise();

  public void setSunrise(Date sunrise);

  public int getHumidity();

  public void setHumidity(int humidity);
}
