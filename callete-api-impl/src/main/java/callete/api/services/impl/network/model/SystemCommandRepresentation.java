package callete.api.services.impl.network.model;

/**
 * Model used for REST
 */
public class SystemCommandRepresentation {
  private String output;
  private String error;

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
