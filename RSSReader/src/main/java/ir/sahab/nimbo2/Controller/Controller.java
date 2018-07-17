package ir.sahab.nimbo2.Controller;

import ir.sahab.nimbo2.model.DatabaseManager;
import ir.sahab.nimbo2.model.NewsRepository;
import ir.sahab.nimbo2.model.Site;
import ir.sahab.nimbo2.model.SiteRepository;
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

  public String update() {
    //TODO
    return null;
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
