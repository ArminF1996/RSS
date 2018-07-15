package ir.sahab.nimbo2;

import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

  public static void main(String[] args) throws SQLException {
    Client client1 = new Client("console");

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


//    Elements rows = null;
//    try {
//      Document doc = Jsoup.connect("https://www.farsnews.com/news/13970423001172/%d8%b9%d8%b1%d8%a8%d8%b3%d8%aa%d8%a7%d9%86-%d8%b3%d8%b9%d9%88%d8%af%db%8c-%d9%87%d8%b2%db%8c%d9%86%d9%87-%d8%ad%d9%85%d9%84%d8%a7%d8%aa-%d8%b1%da%98%db%8c%d9%85-%d8%b5%d9%87%db%8c%d9%88%d9%86%db%8c%d8%b3%d8%aa%db%8c-%d8%a8%d9%87-%d8%ba%d8%b2%d9%87-%d8%b1%d8%a7-%d9%be%d8%b1%d8%af%d8%a7%d8%ae%d8%aa%d9%87-%d8%a7%d8%b3%d8%aa").get();
////      System.out.println(doc);
//      rows = doc.getElementsByAttributeValue("class", "nwstxtmainpane");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    System.out.println(rows.first().text());
  }
}