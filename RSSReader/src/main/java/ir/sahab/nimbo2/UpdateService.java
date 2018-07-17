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
  private int waitTimeout;
  private ConcurrentHashMap<String, String> listOfSitesAndRssUrl;
  private final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE;

  UpdateService(DataBase dbConnector, Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE ,int numberOfThreadsInPool,int waitTimeout) {
    this.dbConnector = dbConnector;
    threadPoolForUpdaters = Executors.newFixedThreadPool(numberOfThreadsInPool);
    listOfSitesAndRssUrl = new ConcurrentHashMap<>();
    this.LOCK_FOR_WAIT_AND_NOTIFY_UPDATE = LOCK_FOR_WAIT_AND_NOTIFY_UPDATE;
    this.waitTimeout=waitTimeout;
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
      synchronized (LOCK_FOR_WAIT_AND_NOTIFY_UPDATE) {
        try {
          LOCK_FOR_WAIT_AND_NOTIFY_UPDATE.wait(waitTimeout);
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
