package ir.sahab.nimbo2.Controller;

import ir.sahab.nimbo2.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

  private static Controller ourInstance = new Controller();

  public static Controller getInstance() {
    return ourInstance;
  }

  private Controller() {}

  public void addExistingSitesToUpdateService() throws SQLException {
    DatabaseUpdateService.getInstance().addSitesFromDatabaseToUpdateService(this.getSitesWithId());
  }

  public String addSite(String rssUrl, String siteName, String siteConfig) throws SQLException {
    return SiteRepository.getInstance().addSitesToDatabase(DatabaseManager.getInstance().getConnection(), new Site(siteName, rssUrl, siteConfig));
  }

  public void update() {
    final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE =
        DatabaseUpdateService.getInstance().getLOCK_FOR_WAIT_AND_NOTIFY_UPDATE();
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

  public int getHistory(int siteID, String dateString) throws SQLException {
    return NewsRepository.getInstance()
        .getNumberOfNewsHistoryForDate(
            DatabaseManager.getInstance().getConnection(), siteID, dateString);
  }

  public ArrayList<HashMap> getTodayInformation() throws SQLException {
    return NewsRepository.getInstance()
        .getNumberOfNewsForToday(
            DatabaseManager.getInstance().getConnection(),
            new Date(new Timestamp(System.currentTimeMillis()).getTime()));
  }
}
