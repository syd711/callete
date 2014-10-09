package callete.api.services.impl.weather;

import callete.api.Callete;
import callete.api.services.weather.Weather;
import callete.api.services.weather.WeatherImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 0	tornado
 * 1	tropical storm
 * 2	hurricane
 * 3	severe thunderstorms
 * 4	thunderstorms
 * 5	mixed rain and snow
 * 6	mixed rain and sleet
 * 7	mixed snow and sleet
 * 8	freezing drizzle
 * 9	drizzle
 * 10	freezing rain
 * 11	showers
 * 12	showers
 * 13	snow flurries
 * 14	light snow showers
 * 15	blowing snow
 * 16	snow
 * 17	hail
 * 18	sleet
 * 19	dust
 * 20	foggy
 * 21	haze
 * 22	smoky
 * 23	blustery
 * 24	windy
 * 25	cold
 * 26	cloudy
 * 27	mostly cloudy (night)
 * 28	mostly cloudy (day)
 * 29	partly cloudy (night)
 * 30	partly cloudy (day)
 * 31	clear (night)
 * 32	sunny
 * 33	fair (night)
 * 34	fair (day)
 * 35	mixed rain and hail
 * 36	hot
 * 37	isolated thunderstorms
 * 38	scattered thunderstorms
 * 39	scattered thunderstorms
 * 40	scattered showers
 * 41	heavy snow
 * 42	scattered snow showers
 * 43	heavy snow
 * 44	partly cloudy
 * 45	thundershowers
 * 46	snow showers
 * 47	isolated thundershowers
 * 3200	not available
 */
public class YahooWeather {
  private final static Logger LOG = LoggerFactory.getLogger(YahooWeather.class);
  private Configuration configuration = Callete.getConfiguration();

  public List<Weather> getWeather() {
    List<Weather> weatherList = new ArrayList<>();
    int count = 0;
    while (true) {
      count++;
      Weather info = getWeatherAt(count);
      if (info != null) {
        weatherList.add(info);
      } else {
        break;
      }
    }
    return weatherList;
  }

  public Weather getWeatherAt(int pos) {
    String url = configuration.getString("weather." + String.valueOf(pos));
    if (!StringUtils.isEmpty(url)) {
      return getWeather(url);
    }
    return null;
  }

  /**
   * Returns the info token of the weather today.
   *
   * @param url The yahoo service URL to retrieve the weather for.
   */
  private Weather getWeather(String url) {
    SyndFeed feed = getFeed(url);
    if (feed != null) {
      return getWeather(feed);
    }
    return null;
  }

  /**
   * Returns the feed for the given URL.
   */
  private SyndFeed getFeed(String url) {
    try {
      URL feedSource = new URL(url);
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(feedSource));
      LOG.info("Requested weather info " + url);
      return feed;
    } catch (Exception e) {
      LOG.error("Error reading weather RSS stream: " + e.getMessage(), e);
    }
    return null;
  }

  /**
   * Returns a weather info about the current weather.
   */
  @SuppressWarnings("unchecked")
  private Weather getWeather(SyndFeed feed) {
    Weather info = new WeatherImpl();
    final List<Element> currentInfo = (List<Element>) feed.getForeignMarkup();
    for (Element element : currentInfo) {
      String name = element.getName();
      if (name.equalsIgnoreCase("location")) {
        info.setCity(element.getAttribute("city").getValue());
        info.setCountry(element.getAttribute("country").getValue());
        break;
      }
    }

    //root elements
    final List<Element> channelElements = (List<Element>) feed.getForeignMarkup();
    for (Element element : channelElements) {
      //wind
      if (element.getName().equalsIgnoreCase("wind")) {
        info.setWind(Double.parseDouble(element.getAttributeValue("speed")));
      }
      //sunrise and sunset
      else if (element.getName().equalsIgnoreCase("astronomy")) {
        try {
          SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
          info.setSunrise(format.parse(element.getAttributeValue("sunrise")));
          info.setSunset(format.parse(element.getAttributeValue("sunset")));
        } catch (ParseException e) {
          LOG.error("Error retrieving sunrise time for " + element.getAttributeValue("sunrise") + ": " + e.getMessage());
        }
      }
    }


    final List<SyndEntry> items = (List<SyndEntry>) feed.getEntries();
    for (SyndEntry entry : items) {
      final List<Element> forecastInfo = (List) entry.getForeignMarkup();
      for (Element element : forecastInfo) {
        //get forecast
        if (element.getName().equalsIgnoreCase("forecast")) {
          Weather forecast = new WeatherImpl();
          String high = element.getAttributeValue("high");
          String low = element.getAttributeValue("low");
          String description = element.getAttributeValue("text");
          int code = Integer.parseInt(element.getAttribute("code").getValue());
          String date = element.getAttribute("date").getValue();
          Weather.WeatherState state = convertTypeCodeWeatherState(code);

          //e.g. Fri, 06 Sep 2013 11:49 am CEST
          Date formatted = null;
          try {
            formatted = new SimpleDateFormat("dd MMM yyyy", Locale.US).parse(date);
          } catch (ParseException e) {
            LOG.error("Error parsing date '" + date + ": " + e.getMessage());
          }

          forecast.setWeatherState(state);
          forecast.setDescription(description);
          forecast.setForecastDate(formatted);
          forecast.setHighTemp(high);
          forecast.setLowTemp(low);

          info.getForecast().add(forecast);
        }
        //get location
        else if (element.getName().equalsIgnoreCase("lat")) {
          info.setLatitude(element.getText());
        } else if (element.getName().equalsIgnoreCase("long")) {
          info.setLatitude(element.getText());
        }
        //get local time
        else if (element.getName().equalsIgnoreCase("condition")) {
          String date = element.getAttributeValue("date");
          try {
            Date formatted = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.US).parse(date);
            info.setLocalTime(formatted);
            info.setTemp(element.getAttributeValue("temp"));
            info.setDescription(element.getAttributeValue("text"));

            int code = Integer.parseInt(element.getAttribute("code").getValue());
            Weather.WeatherState state = convertTypeCodeWeatherState(code);
            info.setWeatherState(state);
          } catch (ParseException e) {
            LOG.error("Error retrieving local time for " + date + ": " + e.getMessage());
          }
        }
      }
    }

    //apply data of first forecast
    Weather todayForecast = info.getForecast().get(0);
    info.setForecastDate(todayForecast.getForecastDate());
    info.setHighTemp(todayForecast.getHighTemp());
    info.setLowTemp(todayForecast.getLowTemp());
    return info;
  }

  private Weather.WeatherState convertTypeCodeWeatherState(Integer code) {
    Weather.WeatherState state = Weather.WeatherState.SUNNY_CLOUDY;
    if (code < 5 || code == 45) {
      state = Weather.WeatherState.STORMY;
    } else if ((code >= 5 && code < 9) || code == 17 || code == 18 || code == 13 || code == 14 || code == 46 || code == 47) {
      state = Weather.WeatherState.SNOW_RAINY;
    } else if ((code >= 15 && code <= 16) || (code >= 40 && code <= 43)) {
      state = Weather.WeatherState.SNOW;
    } else if ((code >= 9 && code < 13) || (code >= 35 && code <= 39)) {
      state = Weather.WeatherState.RAINY;
    } else if (code == 36 || (code >= 32 && code <= 34) || code == 31) {
      state = Weather.WeatherState.SUNNY;
    } else if ((code >= 20 && code <= 25) || code == 19) {
      state = Weather.WeatherState.CLOUDY;
    } else if ((code >= 9 && code < 13)) {
      state = Weather.WeatherState.SUNNY_RAINY;
    } else if ((code >= 29 && code <= 30)) {
      state = Weather.WeatherState.SUNNY_CLOUDY;
    } else if ((code >= 26 && code <= 28) || code == 44) {
      state = Weather.WeatherState.SUNNY_CLOUDS;
    } else {
      LOG.warn("Unmapped weather code: " + code);
    }
    return state;
  }
}
