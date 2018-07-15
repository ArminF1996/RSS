package ir.sahab.nimbo2;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

  public static void main(String[] args) {
//    Client client1 = new Client("console");

    //example
//    Document doc = null;
//    try {
//      doc = Jsoup.connect(
//          "http://www.tabnak.ir/fa/news/816442/%D9%88%D8%B1%D9%88%D8%AF-%D9%85%D8%AC%D9%84%D8%B3-%D8%AF%D8%B1-%D8%B5%D9%88%D8%B1%D8%AA-%D8%B9%D8%AF%D9%85-%D8%A7%D9%86%D8%AA%D8%B4%D8%A7%D8%B1-%D9%81%D9%87%D8%B1%D8%B3%D8%AA-%D8%A7%D8%B1%D8%B2%D8%A8%DA%AF%DB%8C%D8%B1%D8%A7%D9%86")
//          .get();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    Element masthead = doc.select("div.body").first();
//    System.out.println(masthead.text());
//


    Elements rows = null;
    try {
      Document doc = Jsoup.connect("http://www.irna.ir/fa/News/82970770").get();
      rows = doc.getElementsByAttributeValue("class", "BodyText");
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(rows.select("p").first());
  }
}