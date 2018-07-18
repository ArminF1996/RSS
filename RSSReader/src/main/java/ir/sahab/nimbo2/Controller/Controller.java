package ir.sahab.nimbo2.Controller;

import ir.sahab.nimbo2.model.*;

import java.sql.*;

public class Controller {

  private static Controller ourInstance = new Controller();

  public static Controller getInstance() {
    return ourInstance;
  }

  private Controller() {
  }

  public void addSite(String rssUrl, String siteName, String siteConfig) {
    SiteRepository.getInstance().add(new Site(siteName, rssUrl, siteConfig));
  }

  public void update() {
    final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE=DatabaseUpdateService.getInstance().getLOCK_FOR_WAIT_AND_NOTIFY_UPDATE();
    synchronized (LOCK_FOR_WAIT_AND_NOTIFY_UPDATE) {
      LOCK_FOR_WAIT_AND_NOTIFY_UPDATE.notify();
    }
  }

  public ResultSet getSitesWithId() throws SQLException {
    return SiteRepository.getInstance().showAllSites(DatabaseManager.getInstance().getConnection());
  }

  public ResultSet getTenLatestNews(int siteID) throws SQLException {
    return NewsRepository.getInstance()
        .getTenNewestNewsOfSite(DatabaseManager.getInstance().getConnection(), siteID);
  }

  public ResultSet findNewsByBody(String str) throws SQLException {
    return NewsRepository.getInstance()
        .searchByBody(DatabaseManager.getInstance().getConnection(), str);
  }

  public ResultSet findNewsByTitle(String str) throws SQLException {
    return NewsRepository.getInstance()
        .searchByTitle(DatabaseManager.getInstance().getConnection(), str);
  }
}
