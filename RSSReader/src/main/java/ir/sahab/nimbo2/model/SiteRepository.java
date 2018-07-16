package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SiteRepository {

  private ArrayList<Site> mySites;

  public SiteRepository() {
    mySites = new ArrayList<>();
  }

  void add(Site site) {
    mySites.add(site);
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

  void remove(Connection connection, int siteID) {

    PreparedStatement removeSite = null;
    try {
      removeSite = connection.prepareStatement("DELETE FROM Sites WHERE siteID = ? ");
      removeSite.setInt(1, siteID);
      removeSite.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on deleting this site from database.");
    }
  }

  void updateNewsOfSite(Connection connection) {
    //TODO
  }

  void updateConfigOfSite(Connection connection, int siteID, String configSetting) {
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

  ResultSet getNumberOfNewsForToday(Connection connection) {
    //TODO
    return null;
  }

  ResultSet getTenNewestNewsOfSite(Connection connection) {
    //TODO
    return null;
  }

  ResultSet getNumberOfNewsHistoryForPreviousDays(Connection connection) {
    //TODO
    return null;
  }

}
