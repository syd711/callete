package callete.deployment.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * FileUtils used for deployment operations.
 */
public class FileUtils {
  private final static Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  public static void deleteFolder(File folder) throws IOException{
    File[] files = folder.listFiles();
    for(File entry : files) {
      deleteSubFolder(entry);
    }
    LOG.info("Finished deletion of " + folder.getAbsolutePath());
  }

  private static void deleteSubFolder(File folder) throws IOException{
    File[] files = folder.listFiles();
    if(files!=null) { //some JVMs return null for empty dirs
      for(File f: files) {
        if(f.isDirectory()) {
          deleteSubFolder(f);
        } else {
          if(!f.delete()) {
            throw new IOException("Failed to delete " + f.getAbsolutePath());
          }
        }
      }
    }
    if(!folder.delete()) {
      throw new IOException("Failed to delete " + folder.getAbsolutePath());
    }
  }

  public static void unzip(File file, File target) throws IOException {
    ZipFile zipFile = new ZipFile(file);
    Enumeration files = zipFile.entries();
    File f = null;
    FileOutputStream fos = null;

    while (files.hasMoreElements()) {
      try {
        ZipEntry entry = (ZipEntry) files.nextElement();
        InputStream eis = zipFile.getInputStream(entry);
        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        f = new File(target.getAbsolutePath() + File.separator + entry.getName());

        if (entry.isDirectory()) {
          f.mkdirs();
          continue;
        } else {
          f.getParentFile().mkdirs();
          f.createNewFile();
        }

        fos = new FileOutputStream(f);

        while ((bytesRead = eis.read(buffer)) != -1) {
          fos.write(buffer, 0, bytesRead);
        }
      } catch (IOException e) {
        e.printStackTrace();
        continue;
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            // ignore
          }
        }
      }
    }
  }

  static public void zipFolder(File srcFolder, File destZipFile, String[] excludeDirectories) throws Exception {
    FileOutputStream fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
    ZipOutputStream  zip = new ZipOutputStream(fileWriter);

    File[] files = srcFolder.listFiles();
    for(File file : files) {
      addFileToZip("", file.getAbsolutePath(), zip, excludeDirectories);
    }

    zip.flush();
    zip.close();
  }

  static private void addFileToZip(String path, String srcFile, ZipOutputStream zip, String[] excludeDirectories)
          throws Exception {

    File folder = new File(srcFile);
    if (folder.isDirectory()) {
      if(!exclude(folder, excludeDirectories)) {
        addFolderToZip(path, srcFile, zip, excludeDirectories);
      }
    } else {
      byte[] buf = new byte[1024];
      int len;
      FileInputStream in = new FileInputStream(srcFile);
      String entryName = path + "/" + folder.getName();
      if(path.equals("")) {
        entryName = folder.getName();
      }
      ZipEntry entry = new ZipEntry(entryName);
      zip.putNextEntry(entry);
      while ((len = in.read(buf)) > 0) {
        zip.write(buf, 0, len);
      }
    }
  }

  static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip, String[] excludeDirectories)
          throws Exception {
    File folder = new File(srcFolder);

    for (File file : folder.listFiles()) {
      if(exclude(file, excludeDirectories)) {
        continue;
      }
      if (path.equals("")) {
        addFileToZip(folder.getName(), srcFolder + "/" + file.getName(), zip, excludeDirectories);
      } else {
        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + file.getName(), zip, excludeDirectories);
      }
    }
  }

  private static boolean exclude(File file, String[] excludeDirectories) {
    String fileName = file.getName();
    for (String exclude : excludeDirectories) {
      if(exclude.toLowerCase().equals(fileName.toLowerCase())) {
        return true;
      }
      if(file.isFile() && exclude.startsWith(".") && fileName.endsWith(exclude)) {
        return true;
      }
    }
    return false;
  }
}

