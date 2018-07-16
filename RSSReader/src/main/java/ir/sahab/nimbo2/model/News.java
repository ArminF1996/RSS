package ir.sahab.nimbo2.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

public class News {

  int siteID;
  int newsID;
  String title;
  Date publishDay;
  String body;
  String link;

  public News(int siteID, String title, Date publishDay, String body, String link) {
    this.siteID = siteID;
    this.title = title;
    this.publishDay = publishDay;
    this.body = body;
    this.link = link;
  }

  public News(int siteID, int newsID, String title, Date publishDay, String body, String link) {
    this(siteID, title, publishDay, body, link);
    this.newsID = newsID;
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

  public Date getPublishDay() {
    return publishDay;
  }

  public void setPublishDay(Date publishDay) {
    this.publishDay = publishDay;
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
