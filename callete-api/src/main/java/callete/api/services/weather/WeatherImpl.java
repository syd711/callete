package callete.api.services.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the weather interface, simple pojo.
 */
public class WeatherImpl implements Weather {

  private List<Weather> forecast = new ArrayList<Weather>();

  private String city;
  private String country;

  private boolean defaultLocation;

  private String lowTemp;
  private String temp;
  private String highTemp;

  private Date forecastDate;
  private Date localTime;

  private String description;

  private String latitude;
  private String longitude;

  private Date sunrise;
  private Date sunset;

  private double wind;
  private WeatherState weatherState;

  @Override
  public WeatherState getWeatherState() {
    return weatherState;
  }

  @Override
  public void setWeatherState(WeatherState state) {
    this.weatherState = weatherState;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String getLowTemp() {
    return lowTemp;
  }

  @Override
  public void setLowTemp(String lowTemp) {
    this.lowTemp = lowTemp;
  }

  @Override
  public String getHighTemp() {
    return highTemp;
  }

  @Override
  public void setHighTemp(String highTemp) {
    this.highTemp = highTemp;
  }

  @Override
  public Date getForecastDate() {
    return forecastDate;
  }

  @Override
  public void setForecastDate(Date forecastDate) {
    this.forecastDate = forecastDate;
  }

  @Override
  public String getCountry() {
    return country;
  }

  @Override
  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public List<Weather> getForecast() {
    return forecast;
  }

  @Override
  public void setForecast(List<Weather> forecast) {
    this.forecast = forecast;
  }

  @Override
  public String getLatitude() {
    return latitude;
  }

  @Override
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @Override
  public String getLongitude() {
    return longitude;
  }

  @Override
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @Override
  public Date getLocalTime() {
    return localTime;
  }

  @Override
  public void setLocalTime(Date localTime) {
    this.localTime = localTime;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getTemp() {
    return temp;
  }

  @Override
  public void setTemp(String temp) {
    this.temp = temp;
  }

  @Override
  public double getWind() {
    return wind;
  }

  @Override
  public void setWind(double wind) {
    this.wind = wind;
  }

  @Override
  public Date getSunset() {
    return sunset;
  }

  @Override
  public void setSunset(Date sunset) {
    this.sunset = sunset;
  }

  @Override
  public Date getSunrise() {
    return sunrise;
  }

  @Override
  public void setSunrise(Date sunrise) {
    this.sunrise = sunrise;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Weather Information for " + getCity() + ", ");
    builder.append("max " + getHighTemp() + ", ");
    builder.append("min " + getLowTemp() + " at ");
    builder.append(getForecastDate());
    return builder.toString();
  }
}
