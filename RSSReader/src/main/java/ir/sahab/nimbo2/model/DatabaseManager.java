package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseManager {

  private String ip;
  private String port;
  private String dbName;
  private String username;
  private String password;
  private String url;
  private static DatabaseManager databaseManager;
  private BasicDataSource dataSource;

  private DatabaseManager() {
    ip = "localhost";
    port = "3306";
    dbName = "nimroo";
    username = "armin";
    password = "nimroo";
    dataSource = new BasicDataSource();
    setupDataSource(5, 10, 20);
    try {
      createDatabase();
    } catch (SQLException e) {
      System.out.println("can not connecting to database for create tables,"
          + " please check your database-state and config-File and re-run Application!");
      e.printStackTrace();
      System.exit(0);
    }
  }

  /**
   * @author ArminF96
   */
  public static DatabaseManager getInstance() {
    if (databaseManager == null) {
      databaseManager = new DatabaseManager();
    }
    return databaseManager;
  }

  public void setIp(String ip) {
    this.ip = ip;
    createUrl();
    dataSource.setUrl(url);
  }

  public void setPort(String port) {
    this.port = port;
    createUrl();
    dataSource.setUrl(url);
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
    createUrl();
    dataSource.setUrl(url);
  }

  public void setUsername(String username) {
    this.username = username;
    dataSource.setUsername(this.username);
  }

  public void setPassword(String password) {
    this.password = password;
    dataSource.setPassword(this.password);
  }

  private void createUrl() {
    url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName
        + "?autoReconnect=true&useSSL=true&useUnicode=true&characterEncoding=utf-8";
  }

  /**
   * @author ArminF96
   */
  private void setupDataSource(int minIdleConnection, int maxIdleConnection,
      int maxOpenConnection) {
    createUrl();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setUrl(url);
    dataSource.setMinIdle(minIdleConnection);
    dataSource.setMaxIdle(maxIdleConnection);
    dataSource.setMaxOpenPreparedStatements(maxOpenConnection);
  }

  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }

  /**
   * @author ArminF96
   */
  private void createDatabase() throws SQLException {

    String siteEntity =
        "create table if not exists Sites(siteID int PRIMARY KEY AUTO_INCREMENT,"
            + " siteName TINYTEXT CHARACTER SET utf8, rssUrl TINYTEXT CHARACTER SET utf8,"
            + " configSettings varchar(150));";
    String newsEntity =
        "create table if not exists News(newsID int PRIMARY KEY AUTO_INCREMENT, "
            + "link TEXT CHARACTER SET utf8,siteID int FOREIGN KEY REFERENCES Sites(siteID),"
            + " title TEXT CHARACTER SET utf8, publishDate TINYTEXT,"
            + " body MEDIUMTEXT CHARACTER SET utf8);";
    try (Connection connection = DriverManager
        .getConnection(this.url, this.username, this.password)) {
      PreparedStatement createSiteEntity = connection.prepareStatement(siteEntity);
      createSiteEntity.executeUpdate();
      PreparedStatement createNewsEntity = connection.prepareStatement(newsEntity);
      createNewsEntity.executeUpdate();
    }
  }

}