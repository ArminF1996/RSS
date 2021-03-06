package ir.sahab.nimbo2.model;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Site {

  private int siteID;
  private String siteName;
  private String rssUrl;
  private String configSettings;

  public Site(String siteName, String rssUrl, String configSettings) {
    this.siteName = siteName;
    this.rssUrl = rssUrl;
    this.configSettings = configSettings;
  }

  public Site(int siteID, String siteName, String rssUrl, String configSettings) {
    this.siteName = siteName;
    this.rssUrl = rssUrl;
    this.configSettings = configSettings;
    this.siteID = siteID;
  }

  void setSiteID(int siteID) {
    this.siteID = siteID;
  }

  String getSiteName() {
    return siteName;
  }

  String getRssUrl() {
    return rssUrl;
  }

  String getConfigSettings() {
    return configSettings;
  }

  ArrayList<HashMap<String, String>> getRssData()
      throws IOException, SAXException, ParserConfigurationException {
    ArrayList<HashMap<String, String>> rssDataMap = new ArrayList<>();

    Document domTree;
    domTree = getRssXml();
    for (int i = 0; i < domTree.getElementsByTagName("item").getLength(); i++) {
      rssDataMap.add(new HashMap<>());
      for (int j = 0;
          j < domTree.getElementsByTagName("item").item(i).getChildNodes().getLength();
          j++) {
        if (checkTag(domTree, i, j, "title")) {
          rssDataMap.get(i).put("title", contentOfNode(domTree, i, j));
        } else if (checkTag(domTree, i, j, "link")) {
          rssDataMap.get(i).put("link", contentOfNode(domTree, i, j));
        } else if (checkTag(domTree, i, j, "pubDate")) {
          rssDataMap.get(i).put("pubDate", contentOfNode(domTree, i, j));
        }
      }
    }
    return rssDataMap;
  }

  private Document getRssXml() throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
    URL url = new URL(rssUrl);
    URLConnection con = url.openConnection();
    con.setConnectTimeout(5000);
    return domBuilder.parse(con.getInputStream());
  }

  private boolean checkTag(Document domTree, int domNodeNumber, int itemNodeNumber, String tag) {
    return domTree
        .getElementsByTagName("item")
        .item(domNodeNumber)
        .getChildNodes()
        .item(itemNodeNumber)
        .toString()
        .contains(tag);
  }

  private String contentOfNode(Document domTree, int domNodeNumber, int itemNodeNumber) {
    return domTree
        .getElementsByTagName("item")
        .item(domNodeNumber)
        .getChildNodes()
        .item(itemNodeNumber)
        .getTextContent();
  }

  void addNews() throws ParserConfigurationException, SAXException, IOException, SQLException {
    ArrayList<HashMap<String, String>> rssDataMapArray = getRssData();
    for (HashMap news : rssDataMapArray) {
      NewsRepository.getInstance()
          .addNewsToDatabase(
              new News(
                  siteID,
                  (String) news.get("title"),
                  (String) news.get("pubDate"),
                  (String) news.get("link")));
    }
  }
}
