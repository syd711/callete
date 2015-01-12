package callete.deployment.util;

import callete.api.util.SystemUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Generates an executable script to start project that was build via maven.
 */
public class DeploymentArchiver {
  private final static Logger LOG = LoggerFactory.getLogger(DeploymentArchiver.class);

  public static final String LIB_FOLDER = "libs";
  public static final String RUN_SCRIPT_NAME = "run";
  private File deploymentDir;
  private File mainJar;
  private String mainClass;
  private boolean targetOSWindows;
  private String[] resourcesToCopy;
  private boolean quickDeployment;
  private String javaExecuteable;
  private String host;
  private boolean remoteJMX;

  private StringBuilder batchBuffer = new StringBuilder();

  public DeploymentArchiver(File deploymentDir, File mainJar, String mainClasss, String targetOS,
                            String[] resourcesToCopy, boolean quickDeployment, String javaExecuteable,
                            boolean remoteJMX) {
    this.remoteJMX = remoteJMX;
    this.deploymentDir = deploymentDir;
    this.mainJar = mainJar;
    this.mainClass = mainClasss;
    this.targetOSWindows = SystemUtils.isWindows(targetOS);
    this.resourcesToCopy = resourcesToCopy;
    this.quickDeployment = quickDeployment;
    this.javaExecuteable = javaExecuteable;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void generateScript() throws Exception {
    addPrefix();
    addJava();
    addVMParams();
    addClassPath(getLibs());
    addMainJar();
    addMainClass();

    copyResources();
    copyMainJar();
    renameCalleteProperties();
    writeBatchFile();
    writeArchive();
  }

  private void renameCalleteProperties() throws IOException {
    File deploymentProperties = new File(deploymentDir, "conf/callete-deployment.properties");
    File renamedDeploymentProperties = new File(deploymentDir, "conf/callete.properties");
    if(deploymentProperties.exists()) {
      LOG.info("Renaming callete-deployment.properties");
      org.apache.commons.io.FileUtils.copyFile(deploymentProperties, renamedDeploymentProperties);
      deploymentProperties.delete();
    }
  }

  private void addVMParams() {
    if(remoteJMX) {
      batchBuffer.append("-Dcom.sun.management.jmxremote ");
      batchBuffer.append("-Dcom.sun.management.jmxremote.authenticate=false ");
      batchBuffer.append("-Dcom.sun.management.jmxremote.ssl=false ");
      batchBuffer.append("-Dcom.sun.management.jmxremote.port=1100 ");
      batchBuffer.append("-Djava.rmi.server.hostname=" + host + " ");
    }
    batchBuffer.append("-Dfile.encoding=utf8 ");
    batchBuffer.append("-Dlogback.configurationFile=conf/logback.xml ");

  }

  private void copyResources() throws IOException {
    for(String resource : resourcesToCopy) {
      File resourceFile = new File("./", resource);
      LOG.info("Copying " + resourceFile.getAbsolutePath() + " to " + deploymentDir.getAbsolutePath());

      if(resourceFile.isDirectory()) {
        org.apache.commons.io.FileUtils.copyDirectoryToDirectory(resourceFile, deploymentDir);
      } else {
        File target = new File(deploymentDir, resourceFile.getName());
        Files.copy(resourceFile, target);
      }
    }
  }

  private void copyMainJar() throws IOException {
    File target = new File(deploymentDir, mainJar.getName());
    LOG.info("Copying main jar " + target.getAbsolutePath());
    Files.copy(mainJar, new File(deploymentDir, mainJar.getName()));
  }

  private void writeArchive() throws Exception {
    String archiveName = mainJar.getName().substring(0, mainJar.getName().lastIndexOf(".")) + ".zip";
    File tempFile = new File("./", archiveName);
    if(tempFile.exists()) {
      tempFile.delete();
    }
    if(quickDeployment) {
      LOG.info("Archiving main jar for quick deployment.");
      FileUtils.zipFiles(Arrays.asList(mainJar, new File(deploymentDir, "libs"), new File(deploymentDir, "conf")),
          Arrays.asList("jersey", "gson", "guava", "jackson", "jaxb", "js", "st", "slf", "ju",
              "act", "ao", "asm", "gr", "go","java", "log", "mi", "ma", "ro", "com", "gm", "gu", 
              "http", "img", "jd", "jen", "jet", "pi"),
          tempFile);
    }
    else {
      FileUtils.zipFolder(deploymentDir, tempFile, Arrays.asList(".log", ".MF", "classes", "generated-sources", "maven-archiver"));
      LOG.info("Created " + tempFile.getAbsolutePath());
    }
  }

  private void writeBatchFile() throws IOException {
    File batchFile = new File(deploymentDir, getBatchFileName());
    if(batchFile.exists()) {
      batchFile.delete();
    }
    Files.write(batchBuffer.toString(), batchFile, Charsets.UTF_8);
    LOG.info("Written " + batchFile.getAbsolutePath());
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
    for(File f : libs) {
      batchBuffer.append(LIB_FOLDER + "/" + f.getName());
      batchBuffer.append(getCPSeparator());
      LOG.info("Add to classpath: " + f.getName());
    }
  }

  private void addJava() {
    if(StringUtils.isEmpty(javaExecuteable)) {
      batchBuffer.append("java ");
    } else {
      batchBuffer.append(javaExecuteable + " ");
    }
  }

  private void addPrefix() {
    if(targetOSWindows) {
      //ignore
    } else {
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
    if(targetOSWindows) {
      return ";";
    }
    return ":";
  }

  public String getBatchFileName() {
    if(targetOSWindows) {
      return RUN_SCRIPT_NAME + ".bat";
    }
    return RUN_SCRIPT_NAME + ".sh";
  }

  /**
   * Called by the maven executor plugin.
   *
   * @param args params to create the batch script
   */
  public static DeploymentArchiver create(String[] args) throws Exception {
    if(args.length != 9) {
      System.err.println("Invalid number of arguments.");
    }

    LOG.info("************** Creating new deployment ******************");

    String artifactId = args[0];
    String versionId = args[1];
    boolean quickDeployment = Boolean.parseBoolean(args[2]);
    File deploymentDir = new File(args[3]);
    File mainJar = new File(deploymentDir.getParent(), artifactId + "-" + versionId + ".jar");
    String mainClass = args[4];
    String targetOS = args[5];
    String[] resourcesToCopy = args[6].split(",");
    String javaExecuteable = args[7];
    boolean remoteJMX = Boolean.parseBoolean(args[8]);

    DeploymentArchiver runScriptGenerator = new DeploymentArchiver(deploymentDir, mainJar, mainClass, targetOS, resourcesToCopy, quickDeployment, javaExecuteable, remoteJMX);
    LOG.info("************** /Creating new deployment *****************");
    return runScriptGenerator;
  }
}
