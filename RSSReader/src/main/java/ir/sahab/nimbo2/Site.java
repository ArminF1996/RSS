package ir.sahab.nimbo2;

public class Site {

  private String name;
  private String rssUrl;
  private String config;
  private int id;

  public Site(String Name, String url, String config) {
    this.name = Name;
    this.rssUrl = Name;
    this.config = config;
    this.id = 0;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRssUrl() {
    return rssUrl;
  }

  public void setRssUrl(String rssUrl) {
    this.rssUrl = rssUrl;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  public void getNews(){
    //TODO
  }
}
