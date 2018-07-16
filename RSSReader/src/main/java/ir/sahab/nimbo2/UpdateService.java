package ir.sahab.nimbo2;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateService implements Runnable {

  DataBase dbConnector;
  ExecutorService threadPoolForUpdaters;
  ConcurrentHashMap<String, String> listOfSitesAndRssUrl;
  Object LOCKFORWAITANDNOTIFY;

  UpdateService(DataBase dbConnector, Object LOCKFORWAITANDNOTIFY) {
    this.dbConnector = dbConnector;
    threadPoolForUpdaters = Executors.newFixedThreadPool(20);
    listOfSitesAndRssUrl = new ConcurrentHashMap<>();
    this.LOCKFORWAITANDNOTIFY = LOCKFORWAITANDNOTIFY;
  }

  void addSiteForUpdate(String rssUrl, String siteName) {
    listOfSitesAndRssUrl.put(rssUrl, siteName);
  }

  @Override
  public void run() {
    while (true) {
      for (ConcurrentHashMap.Entry<String, String> siteNameAndRssUrl :
          listOfSitesAndRssUrl.entrySet()) {
        threadPoolForUpdaters.submit(
            new UpdateSite(siteNameAndRssUrl.getKey(), siteNameAndRssUrl.getValue()));
      }
      synchronized (LOCKFORWAITANDNOTIFY) {
        try {
          LOCKFORWAITANDNOTIFY.wait(300000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  class UpdateSite implements Runnable {

    String rssUrl;
    String siteName;

    public UpdateSite(String rssUrl, String siteName) {
      this.rssUrl = rssUrl;
      this.siteName = siteName;
    }

    @Override
    public void run() {
      try {
        dbConnector.addNewsForSite(rssUrl, siteName);
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SAXException e) {
        e.printStackTrace();
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      }
    }
  }
}
