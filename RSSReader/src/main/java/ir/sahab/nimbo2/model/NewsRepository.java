package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.codec.digest.DigestUtils;

public class NewsRepository {

  private static NewsRepository ourInstance = new NewsRepository();

  public static NewsRepository getInstance() {
    return ourInstance;
  }

  void addNewsToDatabase(News news) throws SQLException {
    Connection connection;
    connection = DatabaseManager.getInstance().getConnection();

    if (duplicateNews(connection, news)) {
      return;
    }
    PreparedStatement addNews;
    addNews =
        connection.prepareStatement(
            "INSERT INTO news(link, siteID, title, publishDate, body, linkHash) values (?,?,?,?,?,?)");
    addNews.setString(1, news.getLink());
    addNews.setInt(2, news.getSiteID());
    addNews.setString(3, news.getTitle());
    addNews.setTimestamp(4, news.getPublishDate());
    addNews.setString(5, news.getBody());
    addNews.setString(6, getHash(news.getLink()));
    addNews.executeUpdate();
  }

  public ResultSet searchByTitle(Connection connection, String str) throws SQLException {
    PreparedStatement searchTitles =
        connection.prepareStatement("select * from news where title like ?;");
    searchTitles.setString(1, "%" + str + "%");
    return searchTitles.executeQuery();
  }

  public ResultSet searchByBody(Connection connection, String str) throws SQLException {
    PreparedStatement searchBodies =
        connection.prepareStatement("select * from news where body like ?;");
    searchBodies.setString(1, "%" + str + "%");
    ResultSet resultSet = searchBodies.executeQuery();
    return resultSet;
  }

  public ResultSet getTenNewestNewsOfSite(Connection connection, int siteID) throws SQLException {
    PreparedStatement searchBodies =
        connection.prepareStatement(
            "select * from news where siteID = ? order by publishDate DESC LIMIT 10;");
    searchBodies.setInt(1, siteID);
    ResultSet resultSet = searchBodies.executeQuery();
    return resultSet;
  }

  ArrayList<String> getConfig(int siteID) {
    ArrayList<String> ret = new ArrayList<>();
    try {
      Connection connection = DatabaseManager.getInstance().getConnection();
      PreparedStatement getConfig =
          connection.prepareStatement("select configSettings from sites where siteID = ?");
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

  public ArrayList<HashMap> getNumberOfNewsForToday(Connection connection, Date today)
      throws SQLException {
    ArrayList<HashMap> resultMap = new ArrayList<>();
    PreparedStatement getSites = connection.prepareStatement("select * from sites;");
    ResultSet sitesRow = getSites.executeQuery();
    while (sitesRow.next()) {
      HashMap<String, String> hashMap = new HashMap();
      PreparedStatement searchNews =
          connection.prepareStatement(
              "select * from news where siteID = ? order by publishDate desc;");
      searchNews.setInt(1, sitesRow.getInt("siteID"));
      ResultSet searchResultOfNews = searchNews.executeQuery();
      Integer todayNewsCounter = 0;
      while (searchResultOfNews.next()) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(today);
        cal2.setTime(new Date(searchResultOfNews.getTimestamp("publishDate").getTime()));
        boolean sameDay =
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        if (sameDay) {
          todayNewsCounter++;
        } else {
          break;
        }
      }
      hashMap.put("siteID", sitesRow.getString("siteID"));
      hashMap.put("siteName", sitesRow.getString("siteName"));
      hashMap.put("numOfNews", todayNewsCounter.toString());
      resultMap.add(hashMap);
    }
    return resultMap;
  }

  public int getNumberOfNewsHistoryForDate(Connection connection, int siteID, String dateString)
      throws SQLException {
    PreparedStatement searchDates =
        connection.prepareStatement(
            "select * from news where siteID = ? order by publishDate desc;");
    searchDates.setInt(1, siteID);
    ResultSet searchResult = searchDates.executeQuery();
    Date date = getDateWithoutTime(dateString);
    int newsCounter = 0;
    while (searchResult.next()) {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      cal1.setTime(date);
      cal2.setTime(new Date(searchResult.getTimestamp("publishDate").getTime()));
      boolean sameDay =
          cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
              && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
      if (sameDay) {
        newsCounter++;
      } else if (newsCounter != 0) {
        break;
      }
    }
    return newsCounter;
  }

  private Date getDateWithoutTime(String dateString) {
    ArrayList<SimpleDateFormat> formats = new ArrayList<>();
    formats.add(new SimpleDateFormat("dd MMM yyyy"));
    formats.add(new SimpleDateFormat("yyyy-MM-dd"));
    java.util.Date date = null;
    for (SimpleDateFormat formatter : formats) {
      try {
        date = formatter.parse(dateString);
        break;
      } catch (ParseException e) {
        date = new Date();
      }
    }
    if (date == null) {
      date = new Date();
    }
    return date;
  }

  private String getHash(String str) {
    return DigestUtils.md5Hex(str);
  }

  private boolean duplicateNews(Connection connection, News news) {
    try {
      PreparedStatement findDuplicate =
          connection.prepareStatement("select newsID from news where linkHash = ?;");
      findDuplicate.setString(1, getHash(news.getLink()));
      ResultSet resultSet = findDuplicate.executeQuery();
      return resultSet.next();
    } catch (SQLException e) {
      return false;
    }
  }
}
