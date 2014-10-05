package callete.api.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Utility for accessing the different config files.
 */
public class Config {
  private final static Logger LOG = LoggerFactory.getLogger(Config.class);
  private final static String CONFIG_FILENAME = "callete.properties";
  private final static String DEV_CONFIG_FILENAME = "callete-dev.properties";

  public final static String CONFIG_FOLDER = "conf/";
  public final static String DEV_CONFIG_FOLDER = "../conf/";

  public static Configuration getConfiguration() {
    try {
      File configFile = getConfigFile(CONFIG_FOLDER, DEV_CONFIG_FILENAME);
      if(!configFile.exists()) {
        configFile = getConfigFile(CONFIG_FOLDER, CONFIG_FILENAME);
      }
      if(!configFile.exists()) {
        configFile = getConfigFile(DEV_CONFIG_FOLDER, DEV_CONFIG_FILENAME);
      }
      return new PropertiesConfiguration(configFile);
    } catch (Throwable e) {
      LOG.error("Error loading " + CONFIG_FILENAME + ": " + e.getMessage(), e);
    }
    return null;
  }

  private static File getConfigFile(String dir, String config) {
    return new File(dir + config);
  }
}
