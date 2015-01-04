package callete.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Looks up the service interface implementation via reflection.
 * Ensure that only 1x service interface implementation is available in the classpath.
 */
public class ServiceFactory {
  private final static Logger LOG = LoggerFactory.getLogger(ServiceFactory.class);


  public static Service createService(Class clazz) {
    try {      
      String className = clazz.getName() + "Impl";
      //mmpf, plain and simple
      className = className.replace("callete.api.services", "callete.api.services.impl");
      return (Service) Class.forName(className).newInstance();
    } catch (Exception e) {
      LOG.error("Error creating " + clazz + ": " + e.getMessage(), e);
    }

    return null;
  }
}
