package ir.sahab.nimbo2;

import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class App {

  public static void main(String[] args)
      throws SQLException, ParserConfigurationException, SAXException, IOException {
    Client client1 = new Client("console");
  }
}