package callete.api.services.impl.template;

import callete.api.services.template.TemplateSet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.stream.Stream;

public class TempateSetImpl implements TemplateSet {
  private final static Logger LOG = LoggerFactory.getLogger(TempateSetImpl.class);

  private Configuration configuration;

  public TempateSetImpl(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void renderTemplate(String name, Object model, File target) {
    BufferedOutputStream out = null;
    try {
      if(!target.exists()) {
        LOG.info("Template target file " + target.getAbsolutePath() + " not found, creating new file.");
        boolean newFile = target.createNewFile();
        if(!newFile) {
          LOG.error("Failed to create template file " + target.getAbsolutePath());
        }
      }
      
      Template temp = configuration.getTemplate(name);
      out = new BufferedOutputStream(new FileOutputStream(target));
      Writer writer = new OutputStreamWriter(out);
      temp.process(model, writer);
      LOG.info("Written " + target.getAbsolutePath());
    } catch (Exception e) {
      LOG.error("Error rendering template '" + name + "': " + e.getMessage(), e);
    } finally {
      if(out != null) {
        try {
          out.close();
        } catch (IOException e) {
          //
        }
      }
    }
  }

  @Override
  public void renderTemplate(String name, Object model, OutputStream out) {
    try {
      Template temp = configuration.getTemplate(name);
      Writer writer = new OutputStreamWriter(out);
      temp.process(model, writer);
    } catch (Exception e) {
      LOG.error("Error rendering template '" + name + "': " + e.getMessage(), e);
    }
  }
}
