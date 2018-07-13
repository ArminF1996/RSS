package ir.sahab.nimbo2;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class RssDownloader {

  /**
   * this method download the RSS page and save it into file.
   *
   * @param link rssLink
   * @param siteName this name using for create .xml file.
   * @return return the result of action.
   */
  public Boolean downloadRss(String link, String siteName) {

    URL website = null;
    Boolean result = true;

    try {
      website = new URL(link);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return false;
    }
    ReadableByteChannel rbc = null;
    try {
      rbc = Channels.newChannel(website.openStream());
    } catch (IOException e) {
      result = false;
      e.printStackTrace();
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream("src/main/resources/" + siteName + ".xml");
    } catch (FileNotFoundException e) {
      result = false;
      e.printStackTrace();
    }
    try {
      if (fos != null) fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (IOException e) {
      result = false;
      e.printStackTrace();
    } finally {
      try {
        if (fos != null) fos.close();
      } catch (IOException e) {
        result = false;
        e.printStackTrace();
      }
    }
    return result;
  }
}
