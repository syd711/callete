package callete.api.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Utility for accessing the different config files.
 */
public class Settings {
  private final static Logger LOG = LoggerFactory.getLogger(Settings.class);
  private final static String CONFIG_FILENAME = "callete-settings.properties";

  public final static String CONFIG_FOLDER = "conf/";

  private static PropertiesConfiguration settings;

  public static Configuration getSettings() {
    if (settings == null) {
      try {
        File configFile = getConfigFile(CONFIG_FOLDER, CONFIG_FILENAME);
        settings = new PropertiesConfiguration(configFile);
      } catch (Throwable e) {
        LOG.error("Error loading " + CONFIG_FILENAME + ": " + e.getMessage(), e);
      }
    }
    return settings;
  }

  public static void saveSetting(String key, Object value) {
    try {
      getSettings().setProperty(key, value);
      settings.save(getConfigFile(CONFIG_FOLDER, CONFIG_FILENAME));
      LOG.info("Saved setting " + key + " = " + value);
    } catch (ConfigurationException e) {
      LOG.error("Failed to store callete settings: " + e.getMessage(), e);
    }
  }

  private static File getConfigFile(String dir, String config) {
    return new File(dir + config);
  }
}
