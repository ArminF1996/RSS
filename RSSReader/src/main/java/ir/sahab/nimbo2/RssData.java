package ir.sahab.nimbo2;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;

class RssData {

  ArrayList<HashMap<String, String>> getRssData(String link)
      throws ParserConfigurationException, IOException, SAXException {
    ArrayList<HashMap<String, String>> rssDataMap = new ArrayList<>();
    DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder domBuilder = domBuilderFactory.newDocumentBuilder();
    Document domTree = domBuilder.parse(new URL(link).openStream());
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
}
