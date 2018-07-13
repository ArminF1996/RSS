package ir.sahab.nimbo2;

import java.sql.*;

public abstract class DbConnector {

  private static String dbUrl = "jdbc:mysql://localhost:3306/nimroo?autoReconnect=true&useSSL=true";
  private static String userName = "armin";
  private static String passWord = "nimroo";
  private static String createSiteEntity =
      "create table if not exists site(siteID int PRIMARY KEY AUTO_INCREMENT, siteName varchar(50), rssUrl varchar(150), configSettings varchar(100));";
  private static String createHistoryEntity =
      "create table if not exists history(siteID int, date date, numberOfNews int, PRIMARY KEY (siteID,date));";
  private static String createNewsEntity =
      "create table if not exists news(link TEXT PRIMARY KEY, siteID int, title TEXT, publishDate date, body TEXT);";

  public static void setDbUrl(String dbUrl) {
    DbConnector.dbUrl = dbUrl;
  }

  public static void setUserName(String userName) {
    DbConnector.userName = userName;
  }

  public static void setPassWord(String passWord) {
    DbConnector.passWord = passWord;
  }

  public static String getDbUrl() {
    return dbUrl;
  }

  public static String getUserName() {
    return userName;
  }

  /**
   * whit this method, we can create database entities. you can see ERD diagram for more
   * information.
   *
   * @return the result of this commands.
   */
  public static String createEntities() {

    String result;
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(dbUrl, userName, passWord);

      try {
        PreparedStatement siteEntity = connection.prepareStatement(createSiteEntity);
        PreparedStatement historyEntity = connection.prepareStatement(createNewsEntity);
        PreparedStatement newsEntity = connection.prepareStatement(createHistoryEntity);
        siteEntity.executeUpdate();
        historyEntity.executeUpdate();
        newsEntity.executeUpdate();
        result = "Entities Create Successfully.";
      } catch (SQLException e) {
        e.printStackTrace();
        result = "create Entities Failed! Check output console";
      }

    } catch (SQLException e) {
      e.printStackTrace();
      result = "Connection Failed! Check output console";
    } finally {
      try {
        if (connection != null) connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }
}
