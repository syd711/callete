package callete.api.services.impl.template;

import callete.api.services.template.TemplateService;
import callete.api.services.template.TemplateSet;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Template service instance based on FreeMarker.
 */
public class TemplateServiceImpl implements TemplateService {
  private final static Logger LOG = LoggerFactory.getLogger(TemplateServiceImpl.class);

  public static final String ENCODING = "UTF-8";

  @Override
  public TemplateSet createTemplateSetFromPackage(Class clazz, String templatePackage) {
    try {
      Configuration cfg = new Configuration();
      cfg.setClassForTemplateLoading(clazz, templatePackage);
      cfg.setDefaultEncoding(ENCODING);
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      return new TempateSetImpl(cfg);
    } catch (Exception e) {
      LOG.error("Failed to create template set for package " + templatePackage + ": " + e.getMessage(), e);
    }
    return null;
  }

  @Override
  public TemplateSet createTemplateSetFromDirectory(File templateLocation) {
    try {
      Configuration cfg = new Configuration();
      cfg.setDirectoryForTemplateLoading(templateLocation);
      cfg.setDefaultEncoding(ENCODING);
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      return new TempateSetImpl(cfg);
    } catch (Exception e) {
      LOG.error("Failed to create template set for directory " + templateLocation.getAbsolutePath() + ": " + e.getMessage(), e);
    }
    return null;
  }
}
