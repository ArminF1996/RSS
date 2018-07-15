package ir.sahab.nimbo2;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class App {

  public static void main(String[] args) {
//    Client client1 = new Client("console");

    Elements rows = null;
    try {
      Document doc = Jsoup.connect("url").get();
      rows = doc.getElementsByAttributeValue("div","name");
      if (rows != null) {
        System.out.println(rows.first().text());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}