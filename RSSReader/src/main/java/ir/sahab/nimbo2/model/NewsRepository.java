package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class NewsRepository {

  private ArrayList<News> myNews;

  private static NewsRepository ourInstance = new NewsRepository();

  public static NewsRepository getInstance() {
    return ourInstance;
  }

  private NewsRepository() {
    myNews = new ArrayList<>();
  }

  public void add(News news) {
    myNews.add(news);
    if (myNews.size() > 30) {
      try {
        addNewsToDataBase(DatabaseManager.getInstance().getConnection());
      } catch (SQLException e) {
        System.err.println("can not get connection from database.");
      }
    }
  }

  public void addNewsToDataBase(Connection connection) {
    for (News news : myNews) {

      PreparedStatement addNews = null;
      try {
        addNews = connection.prepareStatement(
            "INSERT INTO News(link, siteID, title, publishDate, body) values (?,?,?,?,?)");
        addNews.setString(1, news.getLink());
        addNews.setInt(2, news.getSiteID());
        addNews.setString(3, news.getTitle());
        addNews.setTimestamp(4, news.getPublishDate());
        addNews.setString(5, news.getBody());
        addNews.executeUpdate();
      } catch (SQLException e) {
        System.err.println("failed on adding " + news.getTitle() + " site to database.");
      }
    }
    myNews = null;
  }

  public void remove(Connection connection, int newsID) {
    PreparedStatement removeNews = null;
    try {
      removeNews = connection
          .prepareStatement("DELETE FROM News WHERE newsID = ?");
      removeNews.setInt(1, newsID);
      removeNews.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on deleting this site from database.");
    }
  }

  public ResultSet searchByTitle(Connection connection, String str) throws SQLException {
    PreparedStatement searchTitles = connection
        .prepareStatement("select * from News where title like '% '?' %';");
    searchTitles.setString(1, str);
    return searchTitles.executeQuery();
  }

  public ResultSet searchByBody(Connection connection, String str) throws SQLException {
    PreparedStatement searchBodies = connection
        .prepareStatement("select * from News where body like '% '?' %';");
    searchBodies.setString(1, str);
    return searchBodies.executeQuery();
  }

  public ResultSet getTenNewestNewsOfSite(Connection connection, int siteID) throws SQLException {
    PreparedStatement searchBodies = connection
        .prepareStatement("select * from News where siteID = ? order by publishDate LIMIT 10;");
    searchBodies.setInt(1, siteID);
    return searchBodies.executeQuery();
  }

  public ArrayList<String> getConfig(int siteID) {
    ArrayList<String> ret = new ArrayList<>();
    try {
      Connection connection = DatabaseManager.getInstance().getConnection();
      PreparedStatement getConfig = connection.prepareStatement(
          "select configSettings from sites where siteID = ?");
      getConfig.setInt(1, siteID);
      ResultSet resultSet = getConfig.executeQuery();
      resultSet.next();
      String config = resultSet.getString(1);
      ret = new ArrayList<>(Arrays.asList(config.split("/")));
    } catch (SQLException e) {
      ret.add("class");
      ret.add("body");
    }
    return ret;
  }

  public ResultSet getNumberOfNewsForToday(Connection connection) {
    //TODO
    return null;
  }

  public ResultSet getNumberOfNewsHistoryForPreviousDays(Connection connection) {
    //TODO
    return null;
  }
}
