package callete.deployment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * FileUtils used for deployment operations.
 */
public class FileUtils {
  private final static Logger LOG = LoggerFactory.getLogger(FileUtils.class);
  
  public static void delete(File[] files) {
    for(File file : files) {
      if(file.delete()) {
        LOG.info("Deleted " + file.getAbsolutePath());
      }
      else {
        LOG.error("Deletion failed: " + file.getAbsolutePath());
      }
    }
    
  }

  public static void deleteFolder(File folder, List<String> ignoreList, boolean silently) throws IOException {
    File[] files = folder.listFiles();
    for(File entry : files) {
      deleteSubFolder(entry, ignoreList, silently);
    }
    LOG.info("Finished deletion of " + folder.getAbsolutePath());
  }

  private static void deleteSubFolder(File folder, List<String> ignoreList, boolean silently) throws IOException {
    File[] files = folder.listFiles();
    if(files != null) { //some JVMs return null for empty dirs
      for(File f : files) {
        if(exclude(f, ignoreList)) {
          LOG.info("Ignored directory " + f.getAbsolutePath() + " during deletion.");
          continue;
        }
        if(f.isDirectory()) {
          deleteSubFolder(f, ignoreList, silently);
        } else {
          if(!f.delete()) {
            if(!silently) {
              throw new IOException("Failed to delete " + f.getAbsolutePath());
            } else {
              LOG.error("Failed to delete " + f.getAbsolutePath() + ", ignored silently.");
            }
          }
        }
      }
    }
    if(!folder.delete()) {
      if(!silently) {
        throw new IOException("Failed to delete " + folder.getAbsolutePath());
      } else {
        LOG.error("Failed to delete " + folder.getAbsolutePath() + ", ignored silently.");
      }
    }
  }

  public static void unzip(File file, File target) throws IOException {
    ZipFile zipFile = new ZipFile(file);
    Enumeration files = zipFile.entries();
    File f = null;
    FileOutputStream fos = null;

    while(files.hasMoreElements()) {
      try {
        ZipEntry entry = (ZipEntry) files.nextElement();
        InputStream eis = zipFile.getInputStream(entry);
        byte[] buffer = new byte[1024];
        int bytesRead;

        f = new File(target.getAbsolutePath() + File.separator + entry.getName());

        if(entry.isDirectory()) {
          f.mkdirs();
          continue;
        } else {
          f.getParentFile().mkdirs();
          f.createNewFile();
        }

        fos = new FileOutputStream(f);

        while((bytesRead = eis.read(buffer)) != -1) {
          fos.write(buffer, 0, bytesRead);
        }
      } catch (IOException e) {
        LOG.error("Error unzipping " + file.getAbsolutePath() + ": " + e.getMessage(), e);
      } finally {
        if(fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            // ignore
          }
        }
      }
    }
  }

  public static void zipFile(File srcFile, File destZipFile) throws Exception {
    FileOutputStream fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
    ZipOutputStream zip = new ZipOutputStream(fileWriter);

    addFileToZip("", srcFile.getAbsolutePath(), zip, Collections.emptyList());
    zip.flush();
    zip.close();
  }

  public static void zipFiles(List<File> files, List<String> exclusions, File destZipFile) throws Exception {
    FileOutputStream fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
    ZipOutputStream zip = new ZipOutputStream(fileWriter);

    for(File f : files) {
      if(f.isDirectory()) {
        File[] dirFiles = f.listFiles();
        for(File file : dirFiles) {
          addFileToZip(file.getParentFile().getName(), file.getAbsolutePath(), zip, exclusions);
        }
      }
      else {
        addFileToZip("", f.getAbsolutePath(), zip, Collections.emptyList());
      }
    }
    

    zip.flush();
    zip.close();
  }

  public static void zipFolder(File srcFolder, File destZipFile, List<String> exclusions) throws Exception {
    FileOutputStream fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
    ZipOutputStream zip = new ZipOutputStream(fileWriter);

    File[] files = srcFolder.listFiles();
    for(File file : files) {
      addFileToZip("", file.getAbsolutePath(), zip, exclusions);
    }

    zip.flush();
    zip.close();
  }

  static private void addFileToZip(String path, String srcFile, ZipOutputStream zip, List<String> exclusions)
      throws Exception {

    File file = new File(srcFile);
    if(file.isDirectory()) {
      if(!exclude(file, exclusions)) {
        addFolderToZip(path, srcFile, zip, exclusions);
      }
    } else {
      if(!exclude(file, exclusions)) {
        byte[] buf = new byte[1024];
        int len;
        FileInputStream in = new FileInputStream(srcFile);
        String entryName = path + "/" + file.getName();
        if(path.equals("")) {
          entryName = file.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        zip.putNextEntry(entry);
        while((len = in.read(buf)) > 0) {
          zip.write(buf, 0, len);
        }
      }
    }
  }

  static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip, List<String> exclusions)
      throws Exception {
    File folder = new File(srcFolder);

    for(File file : folder.listFiles()) {
      if(exclude(file, exclusions)) {
        continue;
      }
      if(path.equals("")) {
        addFileToZip(folder.getName(), srcFolder + "/" + file.getName(), zip, exclusions);
      } else {
        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + file.getName(), zip, exclusions);
      }
    }
  }

  private static boolean exclude(File file, List<String> exclusions) {
    String fileName = file.getName();
    for(String exclude : exclusions) {
      if(exclude.toLowerCase().equals(fileName.toLowerCase())) {
        return true;
      }
      if((file.isFile() && exclude.startsWith(".") && fileName.endsWith(exclude))) {
        return true;
      }
      if(file.isFile() && fileName.startsWith(exclude)) {
        return true;
      }
    }
    return false;
  }
}

