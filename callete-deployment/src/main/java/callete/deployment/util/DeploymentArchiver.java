package callete.deployment.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Generates an executable script to start project that was build via maven.
 */
public class DeploymentArchiver {
  public static final String LIB_FOLDER = "libs";
  private File deploymentDir;
  private File mainJar;
  private String mainClass;
  private String targetOS;
  private String[] resourcesToCopy;

  private StringBuilder batchBuffer = new StringBuilder();

  public DeploymentArchiver(File deploymentDir, File mainJar, String mainClasss, String targetOS, String[] resourcesToCopy) {
    this.deploymentDir = deploymentDir;
    this.mainJar = mainJar;
    this.mainClass = mainClasss;
    this.targetOS = targetOS;
    this.resourcesToCopy = resourcesToCopy;
  }

  private void generateScript() throws Exception {
    addPrefix();
    addJava();
    addClassPath(getLibs());
    addMainJar();
    addMainClass();

    copyResources();
    copyMainJar();
    writeBatchFile();
    writeArchive();
  }

  private void copyResources() throws IOException {
    for(String resource : resourcesToCopy) {
      File resourceFile = new File("./", resource);
      System.out.println("Copying " + resourceFile.getAbsolutePath() + " to " + deploymentDir.getAbsolutePath());

      if(resourceFile.isDirectory()) {
        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(resourceFile, deploymentDir);
      }
      else {
        File target = new File(deploymentDir, resourceFile.getName());
        Files.copy(resourceFile, target);
      }
    }
  }

  private void copyMainJar() throws IOException {
    File target = new File(deploymentDir, mainJar.getName());
    System.out.println("Copying main jar " + target.getAbsolutePath());
    Files.copy(mainJar, new File(deploymentDir, mainJar.getName()));
  }

  private void writeArchive() throws Exception {
    String archiveName = mainJar.getName().substring(0, mainJar.getName().lastIndexOf(".")) + ".zip";
    File tempFile = new File("./",  archiveName);
    if(tempFile.exists()) {
      tempFile.delete();
    }
    FileUtils.zipFolder(deploymentDir, tempFile, Arrays.asList(".log", ".MF", "classes", "generated-sources", "maven-archiver"));
    System.out.println("Created " + tempFile.getAbsolutePath());
  }

  private void writeBatchFile() throws IOException {
    File batchFile = new File(deploymentDir, getBatchFileName());
    if(batchFile.exists()) {
      batchFile.delete();
    }
    Files.write(batchBuffer.toString(), batchFile, Charsets.UTF_8);
    System.out.println("Written " + batchFile.getAbsolutePath());
  }

  private void addMainClass() {
    batchBuffer.append(mainClass);
  }

  private void addMainJar() {
    batchBuffer.append(mainJar.getName());
    batchBuffer.append("\" ");
  }

  private void addClassPath(File[] libs) {
    batchBuffer.append("-cp \"");
    for (File f : libs) {
      batchBuffer.append(LIB_FOLDER + "/" + f.getName());
      batchBuffer.append(getCPSeparator());
    }
  }

  private void addJava() {
    batchBuffer.append("java -Xmx128M ");
  }

  private void addPrefix() {
    if(targetOS.toLowerCase().contains("windows")) {
      //ignore
    }
    else {
      batchBuffer.append("sudo ");
    }
  }

  private File[] getLibs() {
    return new File(deploymentDir, LIB_FOLDER).listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".jar");
      }
    });
  }

  public String getCPSeparator() {
    if(targetOS.contains("windows")) {
      return ";";
    }
    return ":";
  }

  public String getBatchFileName() {
    if(targetOS.toLowerCase().contains("windows")) {
      return "run.bat";
    }
    return "run.sh";
  }

  /**
   * Called by the maven executor plugin.
   * @param args params to create the batch script
   */
  public static void main(String[] args) throws Exception {
    if(args.length != 6) {
      System.err.println("Invalid number of arguments.");
    }

    File deploymentDir = new File(args[0]);
    String artifactId = args[1];
    String versionId = args[2];
    File mainJar = new File(deploymentDir.getParent(), artifactId + "-" + versionId + ".jar");
    String mainClass = args[3];
    String targetOS = args[4];
    String[] resourcesToCopy = args[5].split(",");

    DeploymentArchiver runScriptGenerator = new DeploymentArchiver(deploymentDir, mainJar, mainClass, targetOS, resourcesToCopy);
    runScriptGenerator.generateScript();
  }
}
