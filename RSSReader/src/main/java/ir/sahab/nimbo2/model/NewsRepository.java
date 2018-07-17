package ir.sahab.nimbo2.model;

import java.sql.*;
import java.util.*;

public class NewsRepository {

  private ArrayList<News> myNews;

  private static NewsRepository ourInstance = new NewsRepository();

  public static NewsRepository getInstance() {
    return ourInstance;
  }

  private NewsRepository() {
    myNews = new ArrayList<News>();
  }

  public void add(News news) {
    myNews.add(news);
    if (myNews.size() >= 10) {
      try {
        addNewsToDataBase(DatabaseManager.getInstance().getConnection());
      } catch (SQLException e) {
        System.err.println("can not get connection from database.");
      }
    }
  }

  public void addNewsToDataBase(Connection connection) {
    for (News news : myNews) {
      PreparedStatement addNews;

      try {
        addNews = connection.prepareStatement(
            "INSERT INTO news(link, siteID, title, publishDate, body) values (?,?,?,?,?)");
        addNews.setString(1, news.getLink());
        addNews.setInt(2, news.getSiteID());
        addNews.setString(3, news.getTitle());
        addNews.setTimestamp(4, news.getPublishDate());
        addNews.setString(5, news.getBody());
        addNews.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("failed on adding " + news.getTitle() + " news to database.");
      }
    }
    myNews.clear();
  }

  public void remove(Connection connection, int newsID) {
    PreparedStatement removeNews = null;
    try {
      removeNews = connection
          .prepareStatement("DELETE FROM news WHERE newsID = ?");
      removeNews.setInt(1, newsID);
      removeNews.executeUpdate();
    } catch (SQLException e) {
      System.err.println("failed on deleting this site from database.");
    }
  }

  public ResultSet searchByTitle(Connection connection, String str) throws SQLException {
    PreparedStatement searchTitles = connection
        .prepareStatement("select * from news where title like '% '?' %';");
    searchTitles.setString(1, str);
    return searchTitles.executeQuery();
  }

  public ResultSet searchByBody(Connection connection, String str) throws SQLException {
    PreparedStatement searchBodies = connection
        .prepareStatement("select * from news where body like '% '?' %';");
    searchBodies.setString(1, str);
    return searchBodies.executeQuery();
  }

  public ResultSet getTenNewestNewsOfSite(Connection connection, int siteID) throws SQLException {
    PreparedStatement searchBodies = connection
        .prepareStatement("select * from news where siteID = ? order by publishDate DESC LIMIT 10;");
    searchBodies.setInt(1, siteID);
    ResultSet resultSet = searchBodies.executeQuery();
    return resultSet;
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
