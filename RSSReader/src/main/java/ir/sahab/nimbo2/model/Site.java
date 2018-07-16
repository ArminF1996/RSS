package ir.sahab.nimbo2.model;

import java.sql.ResultSet;

public class Site implements SiteRepository{

  int siteID;
  String siteName;
  String rssUrl;
  String configSettings;

  @Override
  public String add() {
    return null;
  }

  @Override
  public String remove() {
    return null;
  }

  @Override
  public String updateNewsOfSite() {
    return null;
  }

  @Override
  public String updateConfigOfSite() {
    return null;
  }

  @Override
  public ResultSet getNumberOfNewsForToday() {
    return null;
  }

  @Override
  public ResultSet getTenNewestNewsOfSite() {
    return null;
  }

  @Override
  public ResultSet getNumberOfNewsHistoryForPreviousDays() {
    return null;
  }
}
