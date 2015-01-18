package callete.api.services.template;

import callete.api.services.Service;

import java.io.File;

/**
 * Service for creating template service instances.
 */
public interface TemplateService extends Service {

  /**
   * Creates a template set using a ClassLoader for template loading.
   * @param clazz used to retrieve the correct ClassLoader for loading.
   * @param templatePackage the actual package to read the templates from.
   * @return the TemplateSet instance to call the render methods on.
   */
  TemplateSet createTemplateSetFromPackage(Class clazz, String templatePackage);

  /**
   * Creates a template set using a specific directory.
   * @param templateLocation the file system location to read the templates from.
   * @return the TemplateSet instance to call the render methods on.
   */
  TemplateSet createTemplateSetFromDirectory(File templateLocation);
}
