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

class RssData {

  ArrayList<HashMap<String, String>> getRssData(String link) {
    ArrayList<HashMap<String, String>> rssDataMap = new ArrayList<>();
    DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder domBuilder;
    try {
      domBuilder = domBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      return null;
    }
    Document domTree;
    try {
      domTree = domBuilder.parse(new URL(link).openStream());
    } catch (SAXException | IOException e) {
      e.printStackTrace();
      return null;
    }
    for (int i = 0; i < domTree.getElementsByTagName("item").getLength(); i++) {
      rssDataMap.add(new HashMap<>());
      for (int j = 0;
          j < domTree.getElementsByTagName("item").item(i).getChildNodes().getLength();
          j++) {
        if (checkTag(domTree, i, j, "title")) {
          rssDataMap
              .get(i)
              .put(
                  "title",
                  domTree
                      .getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        } else if (checkTag(domTree, i, j, "link")) {
          rssDataMap
              .get(i)
              .put(
                  "link",
                  domTree
                      .getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        } else if (checkTag(domTree, i, j, "pubDate")) {
          rssDataMap
              .get(i)
              .put(
                  "pubDate",
                  domTree
                      .getElementsByTagName("item")
                      .item(i)
                      .getChildNodes()
                      .item(j)
                      .getTextContent());
        }
      }
    }
    return rssDataMap;
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
}
