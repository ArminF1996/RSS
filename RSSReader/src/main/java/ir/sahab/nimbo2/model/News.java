package ir.sahab.nimbo2.model;

import java.sql.ResultSet;
import java.util.Date;

public class News implements NewsRepository {

  int siteID;
  int newsID;
  String title;
  Date publishDay;
  String body;
  String link;

  @Override
  public void addNews() {

  }

  @Override
  public void remove() {

  }

  @Override
  public ResultSet searchByTitle() {
    return null;
  }

  @Override
  public ResultSet searchByBody() {
    return null;
  }
}
