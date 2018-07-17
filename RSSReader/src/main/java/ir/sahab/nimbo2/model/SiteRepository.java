package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SiteRepository {

  private ArrayList<Site> mySites;

  private static SiteRepository ourInstance = new SiteRepository();

  public static SiteRepository getInstance() {
    return ourInstance;
  }

  private SiteRepository() {
    mySites = new ArrayList<>();
  }

  public void add(Site site) {
    mySites.add(site);
    if (mySites.size() > 5) {
      try {
        addSitesToDatabase(DatabaseManager.getInstance().getConnection());
      } catch (SQLException e) {
        System.err.println("can not get connection from database.");
      }
    }
  }

  public void addSitesToDatabase(Connection connection) {
    for (Site site : mySites) {

      PreparedStatement addSite = null;
      try {
        addSite = connection.prepareStatement("INSERT INTO Sites(siteName,rssUrl,configSettings)"
            + " values (?,?,?)");
        addSite.setString(1, site.siteName);
        addSite.setString(2, site.rssUrl);
        addSite.setString(3, site.configSettings);
        addSite.executeUpdate();
      } catch (SQLException e) {
        System.err.println("failed on adding " + site.siteName + " site to database.");
      }
    }
    mySites = null;
  }

  public void remove(Connection connection, int siteID) {

    PreparedStatement removeSite = null;
    try {
      removeSite = connection.prepareStatement("DELETE FROM Sites WHERE siteID = ? ");
      removeSite.setInt(1, siteID);
      removeSite.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on deleting this site from database.");
    }
  }

  public void updateNewsOfSite(Connection connection) {
    //TODO
  }

  public void updateConfigOfSite(Connection connection, int siteID, String configSetting) {
    PreparedStatement updateConfigSetting = null;
    try {
      updateConfigSetting = connection
          .prepareStatement("UPDATE Sites SET configSettings = ? WHERE siteID = ?");
      updateConfigSetting.setString(1, configSetting);
      updateConfigSetting.setInt(2, siteID);
      updateConfigSetting.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on updating configSetting of this site in database.");
    }
  }

  public ResultSet showAllSites(Connection connection) {

    PreparedStatement searchBodies = null;
    ResultSet searchResult = null;
    try {
      searchBodies = connection.prepareStatement("select * from sites;");
      searchResult = searchBodies.executeQuery();
    } catch (SQLException e) {
      System.err.println("can not show the list of sites right now, please try again later.");
      return null;
    }
    return searchResult;
  }

}
