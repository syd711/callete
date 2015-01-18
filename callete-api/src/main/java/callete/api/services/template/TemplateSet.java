package callete.api.services.template;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Contains a set of templates, is used to render 
 * different target formats from templates that have been loaded initially.
 */
public interface TemplateSet {

  /**
   * Renders the template with the given name and model.
   * @param name the name of the template file
   * @param model the model/Pojo used for rendering
   * @param target the target file to write the rendered template into.
   */
  void renderTemplate(String name, Object model, File target);

  /**
   * Renders the template with the given name and model.
   * @param name the name of the template file
   * @param model the model/Pojo used for rendering
   * @param out the stream the rendering is written to.
   */
  void renderTemplate(String name, Object model, OutputStream out);
}
