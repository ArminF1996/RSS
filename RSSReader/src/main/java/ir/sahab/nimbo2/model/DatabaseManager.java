package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
  private int maxTotalConnection;
  final static private Logger logger = Logger.getLogger(DatabaseManager.class);

  private DatabaseManager() {
    PropertyConfigurator.configure("log4j.properties");
    Properties configFile = new Properties();
    InputStream fileInput = null;
    try {
      fileInput = new FileInputStream("config.properties");
      configFile.load(fileInput);
      this.maxTotalConnection = Integer.parseInt(configFile.getProperty("maxTotalConnection"));
      this.username = configFile.getProperty("DataBaseUserName");
      this.password = configFile.getProperty("DataBasePassword");
      this.dbName = configFile.getProperty("DataBaseName");
      this.ip = configFile.getProperty("DataBaseIp");
      this.port = configFile.getProperty("DataBasePort");
    } catch (IOException ex) {
      logger.error("end of file Error, please check the config file.", ex);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
        } catch (IOException e) {
          logger.error("end of file Error, please check the config file.", e);
        }
      }
    }
    createUrl();
    dataSource = new BasicDataSource();
    try {
      createDatabase();
    } catch (SQLException e) {
      logger.error("database connection Error, please check the config file.", e);
    }
    setupDataSource(5, 10, 30, maxTotalConnection);
  }

  public void createDatabase() throws SQLException {
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

  private void createUrl() {
    url =
        "jdbc:mysql://"
            + ip
            + ":"
            + port
            + "/"
            + dbName
            + "?autoReconnect=true&useSSL=true&useUnicode=true&characterEncoding=utf-8";
    basicUrl =
        "jdbc:mysql://"
            + ip
            + ":"
            + port
            + "/?user="
            + username
            + "&password="
            + password
            + "&useSSL=true&autoReconnect=true";
  }

  /**
   * @author ArminF96
   */
  private void setupDataSource(
      int minIdleConnection,
      int maxIdleConnection,
      int maxOpenPreparedStatements,
      int totalConnection) {
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
    dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
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
            + " configSettings varchar(150), urlHash varchar(40));";
    String newsEntity =
        "create table if not exists news(newsID int PRIMARY KEY AUTO_INCREMENT, "
            + "link TEXT CHARACTER SET utf8,siteID int,"
            + " title TEXT CHARACTER SET utf8, publishDate TINYTEXT,"
            + " body MEDIUMTEXT CHARACTER SET utf8, linkHash varchar(40),"
            + "FOREIGN KEY (siteID) REFERENCES sites(siteID) ON DELETE CASCADE);";
    try (Connection connection =
        DriverManager.getConnection(this.url, this.username, this.password)) {
      PreparedStatement createSiteEntity = connection.prepareStatement(siteEntity);
      createSiteEntity.executeUpdate();
      PreparedStatement createNewsEntity = connection.prepareStatement(newsEntity);
      createNewsEntity.executeUpdate();
    }
  }
}
