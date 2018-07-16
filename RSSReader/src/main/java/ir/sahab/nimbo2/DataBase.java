package ir.sahab.nimbo2;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
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

  DataBase(String userName, String password) {
    rssData = new RssData();
    createSiteEntity =
        "create table if not exists sites(siteID int PRIMARY KEY AUTO_INCREMENT, siteName TINYTEXT CHARACTER SET utf8,"
            + "rssUrl TINYTEXT CHARACTER SET utf8, configSettings varchar(100));";
    createHistoryEntity =
        "create table if not exists history(siteID int, publishDate TIMESTAMP, numberOfNews int, PRIMARY KEY (siteID,publishDate));";
    createNewsEntity =
        "create table if not exists news(newsID int PRIMARY KEY AUTO_INCREMENT, link TEXT CHARACTER SET utf8, siteID int,"
            + " title TEXT CHARACTER SET utf8, publishDate TIMESTAMP, body MEDIUMTEXT CHARACTER SET utf8);";
    this.userName = userName;
    this.password = password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * whit this method, we can create database entities. you can see ERD diagram for more
   * information.
   *
   * @return the result of this commands.
   */
  public void createEntities() throws SQLException {

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
   * @param rssUrl url of RSS page.
   * @param siteName site's name witch choose by client.
   * @return return the action result.
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

        Date pubDate = getPubDate(tmp.get("pubDate"));
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

  void searchInTitle(String partOfTitle) throws SQLException {
    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      PreparedStatement searchTitles =
          connection.prepareStatement(
              "select * from news where title like '%" + partOfTitle + "%';");
      ResultSet searchResult = searchTitles.executeQuery();
      if (searchResult.getRow() == 0) {
        System.out.println("no title contains this!");
      }
      while (searchResult.next()) {
        System.out.println(searchResult.getString("link"));
      }
    }
  }

  void searchInBody(String partOfBody) throws SQLException {
    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      PreparedStatement searchBodies =
          connection.prepareStatement(
              "select * from news where body like '%" + partOfBody + "%';");
      ResultSet searchResult = searchBodies.executeQuery();
      if (searchResult.getRow() == 0) {
        System.out.println("none of the news contain what u typed!");
      }
      while (searchResult.next()) {
        System.out.println(searchResult.getString("link"));
      }
    }
  }

  void printSitesId() throws SQLException {
    try (Connection connection = DriverManager.getConnection(dbUrl, userName, password)) {
      PreparedStatement searchBodies =
          connection.prepareStatement(
              "select * from sites;");
      ResultSet searchResult = searchBodies.executeQuery();
      if (searchResult.getRow() == 0) {
        System.out.println("none of the news contain what u typed!");
      }
      while (searchResult.next()) {
        System.out
            .println(searchResult.getInt("siteID") + ":" + searchResult.getString("siteName"));
      }
    }
  }

  /**
   * in this method, we convert date from string type to date type.
   * @param dateString  string that contain date.
   * @return  return date that contain pubDate.
   */
  private Date getPubDate(String dateString) {
    System.out.println(dateString);
    ArrayList<SimpleDateFormat> formats = new ArrayList<>();
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm"));
    formats.add(new SimpleDateFormat("dd MMM yyyy hh:mm:ss"));
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss"));
    formats.add(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"));
    Date date = null;
    Boolean flag = false;
    for (SimpleDateFormat formatter : formats) {
      try {
        date = formatter.parse(dateString);
        if (date != null) {
          flag = true;
          break;
        }
      } catch (ParseException e) {

      }
    }
    System.out.println(date+ "\n\n");
    if (!flag) {
      return null;
    }
    return date;
  }
}
