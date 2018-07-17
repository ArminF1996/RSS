package ir.sahab.nimbo2;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

class DataBase {

  private String dbUrl =
      "jdbc:mysql://localhost:3306/nimroo?autoReconnect=true&useSSL=true"
          + "&useUnicode=true&characterEncoding=utf-8";
  private String userName;
  private String password;
  private String createSiteEntity;
  private String createHistoryEntity;
  private String createNewsEntity;
  private RssData rssData;

  /**
   * TODO add to DatabaseManager class not added line remain
   */
  DataBase(String userName, String password) {
    rssData = new RssData();
  }

  /**
   * TODO add to DatabaseManager class not added line remain
   */
  public void createEntities() throws SQLException {
  }

  /**
   * TODO add to SiteRepo class not added line remain
   */
  public void addSite(String rssUrl, String siteName, String config) throws SQLException {

  }

  /**
   * with this method we can add news of specified site to db.
   *
   * @param rssUrl url of RSS page.
   * @param siteName site's name witch choose by client.
   */
  public void addNewsForSite(String rssUrl, String siteName)
      throws SQLException, IOException, SAXException, ParserConfigurationException {
    ArrayList<HashMap<String, String>> rssDataHMap = rssData.getRssData(rssUrl);
    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      siteName = " \"" + siteName + "\"";
      PreparedStatement siteIdPrepareStatement =
          connection.prepareStatement(
              "select siteID,configSettings from sites where siteName=" + siteName);
      ResultSet resultSet = siteIdPrepareStatement.executeQuery();
      resultSet.next();

      int siteID = resultSet.getInt(1);
      String config = resultSet.getString(2);
      String divKey = (new ArrayList<String>(Arrays.asList(config.split("/")))).get(0);
      String divVal = (new ArrayList<String>(Arrays.asList(config.split("/")))).get(1);

      for (HashMap<String, String> tmp : rssDataHMap) {
        PreparedStatement insertData =
            connection.prepareStatement(
                "INSERT  INTO news(link,siteID,title,publishDate,body) values (?,?,?,?,?)");

        insertData.setString(1, tmp.get("link"));
        insertData.setInt(2, siteID);
        insertData.setString(3, tmp.get("title"));

        java.util.Date pubDate = getPubDate(tmp.get("pubDate"));
        java.sql.Timestamp sqlDate = null;
        if (pubDate != null) {
          sqlDate = new java.sql.Timestamp(pubDate.getTime());
        }
        insertData.setTimestamp(4, sqlDate);

        Document doc = Jsoup.connect(tmp.get("link")).get();
        Elements rows = doc.getElementsByAttributeValue(divKey, divVal);
        String body = "main body of news not found!";
        if (rows != null && rows.first() != null) {
          body = rows.first().text();
        }
        insertData.setString(5, body);
        insertData.executeUpdate();
      }
    }
  }

  /**
   * TODO go to newsRepo
   */
  void searchInTitle(String partOfTitle) throws SQLException {
    ResultSet searchResult = null;
    if (searchResult.getRow() == 0) {
      System.out.println("no title contains this!");
    }
    while (searchResult.next()) {
      System.out.println(searchResult.getString("link"));
    }
  }

  /**
   * TODO go to newsRepo
   */
  void searchInBody(String partOfBody) throws SQLException {
    ResultSet searchResult = null;
    if (searchResult.getRow() == 0) {
      System.out.println("none of the news contain what you typed!");
    }
    while (searchResult.next()) {
      System.out.println(searchResult.getString("link"));
    }
  }

  /**
   * TODO add to SiteRepo class not added line remain
   */
  void printSitesId() throws SQLException {

  }

  /**
   * TODO add to SiteRepo class not added line remain
   */
  void printLatestNews(int siteId) throws SQLException {

  }

  void printTodayNewsNumberForEachSite() {
    Date today = Date.valueOf(LocalDate.now());
    System.out.println(today);
  }

  /**
   * in this method, we convert date from string type to date type.
   *
   * @param dateString string that contain date.
   * @return return date that contain pubDate.
   */
  private java.util.Date getPubDate(String dateString) {
    return null;
  }
}
