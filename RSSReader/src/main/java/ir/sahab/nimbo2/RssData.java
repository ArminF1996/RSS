package ir.sahab.nimbo2;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;

public class RssData {

  public ArrayList<HashMap<String, String>> getRssData(String link) {
    ArrayList<HashMap<String, String>> rssDataHMap = new ArrayList<HashMap<String, String>>();
    String url = link;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      return null;
    }
    Document doc = null;
    try {
      doc = db.parse(new URL(url).openStream());
    } catch (SAXException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    for (int i = 0; i < doc.getElementsByTagName("item").getLength(); i++) {
      rssDataHMap.add(new HashMap<String, String>());
      for (int j = 0;
          j < doc.getElementsByTagName("item").item(i).getChildNodes().getLength();
          j++) {
        if (doc.getElementsByTagName("item")
            .item(i)
            .getChildNodes()
            .item(j)
            .toString()
            .contains("title")) {
          rssDataHMap
              .get(i)
              .put(
                  "title",
                  doc.getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        } else if (doc.getElementsByTagName("item")
            .item(i)
            .getChildNodes()
            .item(j)
            .toString()
            .contains("link")) {
          rssDataHMap
              .get(i)
              .put(
                  "link",
                  doc.getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        } else if (doc.getElementsByTagName("item")
            .item(i)
            .getChildNodes()
            .item(j)
            .toString()
            .contains("pubDate")) {
          rssDataHMap
              .get(i)
              .put(
                  "pubDate",
                  doc.getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        }
      }
    }
    return rssDataHMap;
  }
}
