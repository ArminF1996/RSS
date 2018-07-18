package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.codec.digest.DigestUtils;

public class SiteRepository {

  private static SiteRepository ourInstance = new SiteRepository();

  public static SiteRepository getInstance() {
    return ourInstance;
  }

  private SiteRepository() {

  }

  public void addSitesToDatabase(Connection connection, Site site) {
    try {
      addToDatabase(connection, site);
      findAndSetSiteIDFromDatabase(connection, site);
      final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE =
          DatabaseUpdateService.getInstance().getLOCK_FOR_WAIT_AND_NOTIFY_UPDATE();
      synchronized (LOCK_FOR_WAIT_AND_NOTIFY_UPDATE) {
        LOCK_FOR_WAIT_AND_NOTIFY_UPDATE.notify();
      }
    } catch (SQLException e) {
      System.err.println("failed on adding " + site.getSiteName() + " site to database.");
    }
  }

  private void addToDatabase(Connection connection, Site site) throws SQLException {
    PreparedStatement addSite = null;
    addSite =
        connection.prepareStatement(
            "INSERT INTO sites(siteName, rssUrl, configSettings, urlHash)" + " values (?,?,?,?)");
    addSite.setString(1, site.getSiteName());
    addSite.setString(2, site.getRssUrl());
    addSite.setString(3, site.getConfigSettings());
    addSite.setString(4, getHash(site.getRssUrl()));
    addSite.executeUpdate();
  }

  public void remove(Connection connection, int siteID) {

    PreparedStatement removeSite = null;
    try {
      removeSite = connection.prepareStatement("DELETE FROM sites WHERE siteID = ? ");
      removeSite.setInt(1, siteID);
      removeSite.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on deleting this site from database.");
    }
  }

  public void updateConfigOfSite(Connection connection, int siteID, String configSetting) {
    PreparedStatement updateConfigSetting = null;
    try {
      updateConfigSetting =
          connection.prepareStatement("UPDATE sites SET configSettings = ? WHERE siteID = ?");
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

  public void findAndSetSiteIDFromDatabase(Connection connection, Site site) {
    PreparedStatement getSiteID = null;
    try {
      getSiteID = connection.prepareStatement("select siteID from sites where siteName = ?;");
      getSiteID.setString(1, site.getSiteName());
      ResultSet resultSet = getSiteID.executeQuery();
      if (resultSet.next()) {
        site.setSiteID(resultSet.getInt("siteID"));
      }
    } catch (SQLException e) {
      site.setSiteID(0);
    }
  }

  private String getHash(String str) {
    return DigestUtils.md5Hex(str);
  }
}
