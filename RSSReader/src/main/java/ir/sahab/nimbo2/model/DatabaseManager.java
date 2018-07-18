package ir.sahab.nimbo2.model;

import java.sql.*;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseManager {

  private String ip;
  private String basicUrl;
  private String port;
  private String dbName;
  private String username;
  private String password;
  private String url;
  private static DatabaseManager databaseManager;
  private BasicDataSource dataSource;
  private int maxTotalConnection = 30;

  private DatabaseManager() {
    ip = "localhost";
    port = "3306";
    dbName = "dbNimroo";
    username = "armin";
    password = "nimroo";
    createUrl();
    dataSource = new BasicDataSource();
    try {
      createDatabase();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("can not connecting to database for create tables,"
          + " please check your database-state and config-File and re-run Application!");
    }
    setupDataSource(10, 10, 20, maxTotalConnection);
  }

  private void createDatabase() throws SQLException {
    Connection Conn = DriverManager.getConnection(basicUrl);
    Statement s = Conn.createStatement();
    s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
    s.executeUpdate("set global max_connections = " + maxTotalConnection * 2);
    createDatabaseEntities();
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
    basicUrl = "jdbc:mysql://" + ip + ":" + port
        + "/?user=" + username + "&password=" + password + "&useSSL=true&autoReconnect=true";
  }

  /**
   * @author ArminF96
   */
  private void setupDataSource(int minIdleConnection, int maxIdleConnection,
      int maxOpenConnection, int totalConnection) {
    createUrl();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setDefaultAutoCommit(true);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setUrl(url);
    dataSource.setRemoveAbandonedTimeout(1);
    dataSource.setSoftMinEvictableIdleTimeMillis(20);
    dataSource.setMaxTotal(totalConnection);
    dataSource.setRemoveAbandonedOnBorrow(true);
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
  private void createDatabaseEntities() throws SQLException {

    String siteEntity =
        "create table if not exists sites(siteID int PRIMARY KEY AUTO_INCREMENT,"
            + " siteName TINYTEXT CHARACTER SET utf8, rssUrl TINYTEXT CHARACTER SET utf8,"
            + " configSettings varchar(150));";
    String newsEntity =
        "create table if not exists news(newsID int PRIMARY KEY AUTO_INCREMENT, "
            + "link TEXT CHARACTER SET utf8,siteID int,"
            + " title TEXT CHARACTER SET utf8, publishDate TINYTEXT,"
            + " body MEDIUMTEXT CHARACTER SET utf8,"
            + "FOREIGN KEY (siteID) REFERENCES sites(siteID));";
    try (Connection connection = DriverManager
        .getConnection(this.url, this.username, this.password)) {
      PreparedStatement createSiteEntity = connection.prepareStatement(siteEntity);
      createSiteEntity.executeUpdate();
      PreparedStatement createNewsEntity = connection.prepareStatement(newsEntity);
      createNewsEntity.executeUpdate();
    }
  }

}
