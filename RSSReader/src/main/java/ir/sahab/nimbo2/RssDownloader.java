package ir.sahab.nimbo2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class RssDownloader {

  private void downloadRss(String link) {
    URL website = null;
    try {
      website = new URL(link);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    ReadableByteChannel rbc = null;
    try {
      rbc = Channels.newChannel(website.openStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream("information.xml");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
