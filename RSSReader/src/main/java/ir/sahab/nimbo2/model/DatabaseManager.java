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
    setupDataSource();
    try {
      createDatabase();
    } catch (SQLException e) {
      System.out.println("can not connecting to database for create tables,"
          + " please check your database-state and config-File and re-Run Application!");
      e.printStackTrace();
      System.exit(0);
    }
  }

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

  private void setupDataSource() {
    createUrl();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setUrl(url);
    dataSource.setMinIdle(5);
    dataSource.setMaxIdle(10);
    dataSource.setMaxOpenPreparedStatements(20);
  }

  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }

  private void createDatabase() throws SQLException {

    String siteEntity =
        new StringBuilder()
            .append("create table if not exists Sites(siteID int PRIMARY KEY AUTO_INCREMENT,")
            .append(" siteName TINYTEXT CHARACTER SET utf8, rssUrl TINYTEXT CHARACTER SET utf8,")
            .append(" configSettings varchar(150));").toString();
    String newsEntity =
        new StringBuilder()
            .append("create table if not exists News(newsID int PRIMARY KEY AUTO_INCREMENT, ")
            .append("link TEXT CHARACTER SET utf8,siteID int FOREIGN KEY REFERENCES Sites(siteID),")
            .append(" title TEXT CHARACTER SET utf8, publishDate TINYTEXT,")
            .append(" body MEDIUMTEXT CHARACTER SET utf8);").toString();
    try (Connection connection = DriverManager
        .getConnection(this.url, this.username, this.password)) {
      PreparedStatement createSiteEntity = connection.prepareStatement(siteEntity);
      createSiteEntity.executeUpdate();
      PreparedStatement createNewsEntity = connection.prepareStatement(newsEntity);
      createNewsEntity.executeUpdate();
    }
  }

}
