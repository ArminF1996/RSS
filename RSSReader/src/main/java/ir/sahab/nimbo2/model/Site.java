package ir.sahab.nimbo2.model;

public class Site {

  int siteID;
  String siteName;
  String rssUrl;
  String configSettings;

  public Site(String siteName, String rssUrl, String configSettings) {
    this.siteName = siteName;
    this.rssUrl = rssUrl;
    this.configSettings = configSettings;
  }

  public Site(int siteID, String siteName, String rssUrl, String configSettings) {
    this(siteName, rssUrl, configSettings);
    this.siteID = siteID;
  }

  public int getSiteID() {
    return siteID;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getRssUrl() {
    return rssUrl;
  }

  public void setRssUrl(String rssUrl) {
    this.rssUrl = rssUrl;
  }

  public String getConfigSettings() {
    return configSettings;
  }

  public void setConfigSettings(String configSettings) {
    this.configSettings = configSettings;
  }
}
