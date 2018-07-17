package ir.sahab.nimbo2.model;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class News {

  private int siteID;
  private int newsID;
  private String title;
  private Timestamp publishDate;
  private String body;
  private String link;

  public News(int siteID, String title, String publishDate, String link) {
    this.siteID = siteID;
    this.title = title;
    this.publishDate = getSqlTimeStamp(reFormatPublishDate(publishDate));
    this.link = link;
    body = curlBody();
  }

  public News(int siteID, int newsID, String title, String publishDate, String link) {
    this(siteID, title, publishDate, link);
    this.newsID = newsID;
  }

  public News(int siteID, int newsID, String title, Timestamp publishDate, String body,
      String link) {
    this.siteID = siteID;
    this.newsID = newsID;
    this.title = title;
    this.publishDate = publishDate;
    this.body = body;
    this.link = link;
  }

  private String curlBody() {
    String body;
    try {
      Document doc = Jsoup.connect(link).get();
      ArrayList<String> config = NewsRepository.getInstance().getConfig(siteID);
      Elements rows = doc.getElementsByAttributeValue(config.get(0), config.get(1));
      body = rows.first().text();
    } catch (IOException e) {
      body = "main body of news not found!";
    }
    return body;
  }

  private Timestamp getSqlTimeStamp(Date pubDate) {
    return new java.sql.Timestamp(pubDate.getTime());
  }

  private Date reFormatPublishDate(String pubDate) {
    ArrayList<SimpleDateFormat> formats = new ArrayList<>();
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm"));
    formats.add(new SimpleDateFormat("dd MMM yyyy hh:mm:ss"));
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss"));
    formats.add(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"));
    Date date = null;
    for (SimpleDateFormat formatter : formats) {
      try {
        date = formatter.parse(pubDate);
        break;
      } catch (ParseException e) {
        date = new Date();
      }
    }
    return date;
  }

  public int getNewsID() {
    return newsID;
  }

  public int getSiteID() {
    return siteID;
  }

  public void setSiteID(int siteID) {
    this.siteID = siteID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Timestamp getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(Timestamp publishDate) {
    this.publishDate = publishDate;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }
}
