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

class News {

  private int siteID;
  private String title;
  private Timestamp publishDate;
  private String body;
  private String link;

  News(int siteID, String title, String publishDate, String link) {
    this.siteID = siteID;
    this.title = title;
    this.publishDate = getSqlTimeStamp(reFormatPublishDate(publishDate));
    this.link = link;
    body = curlBody();
  }

  private String curlBody() {
    String body;
    try {
      Document doc = Jsoup.connect(link).timeout(5000).get();
      ArrayList<String> config = NewsRepository.getInstance().getConfig(siteID);
      Elements rows = doc.getElementsByAttributeValue(config.get(0), config.get(1));
      body = rows.first().text();
    } catch (IOException
        | NullPointerException
        | ExceptionInInitializerError
        | IndexOutOfBoundsException e) {
      body = "main body of news not found!";
    }
    return body;
  }

  private Timestamp getSqlTimeStamp(Date pubDate) {
    return new java.sql.Timestamp(pubDate.getTime());
  }

  private Date reFormatPublishDate(String pubDate) {
    ArrayList<SimpleDateFormat> formats = new ArrayList<>();
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm Z"));
    formats.add(new SimpleDateFormat("dd MMM yyyy hh:mm:ss Z"));
    formats.add(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z"));
    formats.add(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss Z"));
    Date date = null;
    if (pubDate == null) {
      pubDate = (new Date()).toString();
    }
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

  int getSiteID() {
    return siteID;
  }

  String getTitle() {
    return title;
  }

  Timestamp getPublishDate() {
    return publishDate;
  }

  String getBody() {
    return body;
  }

  String getLink() {
    return link;
  }
}
