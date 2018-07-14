package ir.sahab.nimbo2;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DbConnector {

  private static String dbUrl = "jdbc:mysql://localhost:3306/nimroo?autoReconnect=true&useSSL=true"
      + "&useUnicode=true&characterEncoding=utf-8";
  private static String userName = "armin";
  private static String password = "nimroo";
  private static String createSiteEntity =
      "create table if not exists sites(siteID int PRIMARY KEY AUTO_INCREMENT, siteName TINYTEXT CHARACTER SET utf8,"
          + "rssUrl TINYTEXT CHARACTER SET utf8, configSettings varchar(100));";
  private static String createHistoryEntity =
      "create table if not exists history(siteID int, date date, numberOfNews int, PRIMARY KEY (siteID,date));";
  private static String createNewsEntity =
      "create table if not exists news(newsID int PRIMARY KEY AUTO_INCREMENT, link TEXT CHARACTER SET utf8, siteID int,"
          +
          " title TEXT CHARACTER SET utf8, publishDate TINYTEXT, body MEDIUMTEXT CHARACTER SET utf8);";

  public static void setPassword(String passWord) {
    DbConnector.password = passWord;
  }

  public static String getDbUrl() {
    return dbUrl;
  }

  public static void setDbUrl(String dbUrl) {
    DbConnector.dbUrl = dbUrl;
  }

  public static String getUserName() {
    return userName;
  }

  public static void setUserName(String userName) {
    DbConnector.userName = userName;
  }

  /**
   * whit this method, we can create database entities. you can see ERD diagram for more
   * information.
   *
   * @return the result of this commands.
   */
  public static void createEntities() throws SQLException {

    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      PreparedStatement siteEntity = connection.prepareStatement(createSiteEntity);
      siteEntity.executeUpdate();
      PreparedStatement historyEntity = connection.prepareStatement(createNewsEntity);
      historyEntity.executeUpdate();
      PreparedStatement newsEntity = connection.prepareStatement(createHistoryEntity);
      newsEntity.executeUpdate();
    }
  }

  /**
   * with this method, the clients can add new site to db.
   *
   * @param rssUrl url of news agency's rssPage.
   * @param siteName news agency site's name.
   * @param config config of this site. TODO
   * @return return the result of adding new site.
   */
  public void addSite(String rssUrl, String siteName, String config) throws SQLException {

    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      PreparedStatement addSite =
          connection.prepareStatement(
              "INSERT  INTO sites  (siteName,rssUrl,configSettings) values (?,?,?)");
      addSite.setString(1, siteName);
      addSite.setString(2, rssUrl);
      addSite.setString(3, config);
      addSite.executeUpdate();
    }
  }

  /**
   * with this method we can add news of specified site to db.
   *
   * @param hmArr array list of hashMap for each Item.
   * @param siteName site's name witch choose by client.
   * @return return the action result.
   */
  public void addNewsForSite(ArrayList<HashMap<String, String>> hmArr, String siteName)
      throws SQLException {

    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      int siteID = 0;
      siteName = " \"" + siteName + "\"";
      PreparedStatement siteIdPrepareStatement = connection
          .prepareStatement("select siteID from sites where siteName=" + siteName);
      ResultSet resultSet = siteIdPrepareStatement.executeQuery();
      resultSet.next();
      siteID = resultSet.getInt(1);
      for (HashMap<String, String> hm : hmArr) {
        PreparedStatement insertData = connection.prepareStatement(
            "INSERT  INTO news(link,siteID,title,publishDate,body) values (?,?,?,?,?)");

        insertData.setString(1, hm.get("link"));
        insertData.setInt(2, siteID);
        insertData.setString(3, hm.get("title"));
        insertData.setString(4, hm.get("pubDate"));
        insertData.setString(5, "test");

        insertData.executeUpdate();
      }
    }
  }
}
