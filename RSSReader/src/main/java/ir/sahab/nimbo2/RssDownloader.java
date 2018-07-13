package ir.sahab.nimbo2;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class RssDownloader {

  public String downloadRss(String link) {

    URL website = null;
    String result = "action was successful.";

    try {
      website = new URL(link);
    } catch (MalformedURLException e) {
      result = "link is not valid.";
      e.printStackTrace();
    }
    ReadableByteChannel rbc = null;
    try {
      rbc = Channels.newChannel(website.openStream());
    } catch (IOException e) {
      result = "open stream failed.";
      e.printStackTrace();
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream("information.xml");
    } catch (FileNotFoundException e) {
      result = "openning file failed.";
      e.printStackTrace();
    }
    try {
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (IOException e) {
      result = "xml fetching failed.";
      e.printStackTrace();
    }
    return result;
  }
}
