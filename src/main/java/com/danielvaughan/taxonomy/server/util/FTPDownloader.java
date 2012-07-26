package com.danielvaughan.taxonomy.server.util;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FTPDownloader {

  private final String SERVER = "ftp.ebi.ac.uk";
  private final String FILE_PATH = "/pub/databases/ena/taxonomy/";
  private final String GZ_FILE_NAME = "taxonomy.xml.gz";
  private final String FILE_NAME = "taxonomy.xml";

  FTPClient ftpClient = new FTPClient();
  int reply;

  public File download() {
    File file = null;
    boolean error = false;
    try {
      int reply;
      ftpClient.connect(SERVER);
      System.out.println("Connected to " + SERVER + ".");
      System.out.print(ftpClient.getReplyString());

      // After connection attempt, you should check the reply code to verify
      // success.
      reply = ftpClient.getReplyCode();

      if (!FTPReply.isPositiveCompletion(reply)) {
        ftpClient.disconnect();
        System.err.println("FTP server refused connection.");
        System.exit(1);
      } else {
        ftpClient.login("anonymous", "");
        System.out.println("login?" + ftpClient.getReplyCode());
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        System.out.println("binary?" + ftpClient.getReplyCode());
        final FileOutputStream fos = new FileOutputStream(GZ_FILE_NAME);
        ftpClient.setBufferSize(1024);
        ftpClient.enterLocalPassiveMode();
        ftpClient.enterLocalActiveMode();
        ftpClient.changeWorkingDirectory(FILE_PATH);
        System.out.println("cd?" + ftpClient.getReplyCode());
        //removeExisting();
        ftpClient.retrieveFile(GZ_FILE_NAME, fos);
        System.out.println("retrieveFile?" + ftpClient.getReplyCode());
        fos.close();
      }
      ftpClient.logout();
      file = uncompress();
    } catch (final IOException e) {
      error = true;
      e.printStackTrace();
    } finally {
      if (ftpClient.isConnected()) {
        try {
          ftpClient.disconnect();
        } catch (final IOException ioe) {
          // do nothing
        }
      }
    }
    return file;
  }

  private void removeExisting() {
    try {
      File downloadFile = new File(GZ_FILE_NAME);
      File taxonomyFile = new File(FILE_NAME);
      if (downloadFile.exists()) {
        System.out.println("Deleting " + downloadFile.getCanonicalPath());
        downloadFile.delete();
      }
      if (taxonomyFile.exists()) {

        System.out.println("Deleting " + taxonomyFile.getCanonicalPath());
        taxonomyFile.delete();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File uncompress() {
    File file = null;
    try {
      final FileInputStream fin = new FileInputStream(GZ_FILE_NAME);
      final BufferedInputStream in = new BufferedInputStream(fin);
      final FileOutputStream out = new FileOutputStream(FILE_NAME);
      final GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
      IOUtils.copy(gzIn, out);
      out.close();
      gzIn.close();
      file = new File(FILE_NAME);
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return file;
  }

}
