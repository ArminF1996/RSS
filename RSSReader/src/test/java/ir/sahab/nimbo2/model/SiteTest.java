package ir.sahab.nimbo2.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SiteTest {
  ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStream() {
    System.setErr(new PrintStream(errContent));
  }

  @Test
  public void getRssDataGettingAllRssTest() throws ParserConfigurationException, SAXException, IOException {
    Site testSite = new Site("isna","https://www.isna.ir/rss","class/item-body content-full-news");
    ArrayList<HashMap<String, String>> rssDataMapArray = testSite.getRssData();
    Assert.assertEquals(30,rssDataMapArray.size());
  }

  @Test(expected = IOException.class)
  public void getRssDataWithWrongRssTest() throws ParserConfigurationException, SAXException, IOException {
    Site testSite = new Site("isna","https://www.isna.ir/rs","class/item-body content-full-news");
    testSite.getRssData();
    Assert.assertEquals("filed on Parsing xml, check your network connection.\n",errContent.toString());
  }

  @Test(expected = IOException.class)
  public void addNewsWithWrongRssTest() throws SQLException, ParserConfigurationException, SAXException, IOException {
    Site testSite = new Site("isna","https://www.isna.ir/rs","class/item-body content-full-news");
    testSite.addNews();
    Assert.assertEquals("filed on Parsing xml, check your network connection.\n",errContent.toString());
  }

  @After
  public void restoreStream() {
    System.setErr(System.err);
  }
}