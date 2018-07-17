package ir.sahab.nimbo2;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateService implements Runnable {
  private DataBase dbConnector;
  ExecutorService threadPoolForUpdaters;
  private ConcurrentHashMap<String, String> listOfSitesAndRssUrl;
  private final Object LOCK_FOR_WAIT_AND_NOTIFY;

  UpdateService(DataBase dbConnector, Object LOCK_FOR_WAIT_AND_NOTIFY) {
    this.dbConnector = dbConnector;
    threadPoolForUpdaters = Executors.newFixedThreadPool(20);
    listOfSitesAndRssUrl = new ConcurrentHashMap<>();
    this.LOCK_FOR_WAIT_AND_NOTIFY = LOCK_FOR_WAIT_AND_NOTIFY;
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
      synchronized (LOCK_FOR_WAIT_AND_NOTIFY) {
        try {
          LOCK_FOR_WAIT_AND_NOTIFY.wait(300000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  class UpdateSite implements Runnable {
    String rssUrl;
    String siteName;

    UpdateSite(String rssUrl, String siteName) {
      this.rssUrl = rssUrl;
      this.siteName = siteName;
    }

    @Override
    public void run() {
      try {
        dbConnector.addNewsForSite(rssUrl, siteName);
      } catch (SQLException | IOException | ParserConfigurationException | SAXException e) {
        e.printStackTrace();
      }
    }
  }
}
