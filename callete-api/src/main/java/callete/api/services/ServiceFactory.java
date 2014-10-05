package callete.api.services;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Looks up the service interface implementation via reflection.
 * Ensure that only 1x service interface implementation is available in the classpath.
 * Furthermore, the reflection looks ups up only packages starting with "callete".
 */
public class ServiceFactory {
  private final static Logger LOG = LoggerFactory.getLogger(ServiceFactory.class);

  private final static String SCANNER_PACKAGE = "callete.api.services.impl";
  //contains the prefetched packages names where service implementations should be looked up.
  private static Set<String> embeddedPackages;

  public static Service createService(@Nonnull Class clazz) {
    try {
      if (embeddedPackages == null) {
        embeddedPackages = findAllPackagesStartingWith(SCANNER_PACKAGE);
      }
      for (String packageName : embeddedPackages) {
        Reflections reflections = new Reflections(packageName);
        Set subTypesOf = reflections.getSubTypesOf(clazz);
        if (!subTypesOf.isEmpty()) {
          Class next = (Class) subTypesOf.iterator().next();
          LOG.info("Created services class " + clazz.getName());
          return (Service) next.newInstance();
        }
      }
    } catch (Exception e) {
      LOG.error("Error creating " + clazz + ": " + e.getMessage(), e);
    }

    return null;
  }

  /**
   * Initially filters the package look list only to those starting with the given prefix.
   * @param prefix the package prefix to look for.
   * @return a set of package names that match the filter criteria.
   */
  private static Set<String> findAllPackagesStartingWith(String prefix) {
    List<ClassLoader> classLoadersList = new LinkedList<>();
    classLoadersList.add(ClasspathHelper.contextClassLoader());
    classLoadersList.add(ClasspathHelper.staticClassLoader());
    Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false), new ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()])))
            .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix))));
    Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

    Set<String> packageNameSet = new TreeSet<>();
    for (Class classInstance : classes) {
      String packageName = classInstance.getPackage().getName();
      if (packageName.startsWith(prefix)) {
        packageNameSet.add(packageName);
      }
    }
    return packageNameSet;
  }
}
