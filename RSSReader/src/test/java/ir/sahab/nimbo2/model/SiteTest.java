package ir.sahab.nimbo2.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SiteTest {
  ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStream() {
    System.setErr(new PrintStream(errContent));
  }

  @Test
  public void getRssDataGettingAllRssTest() {
    Site testSite = new Site("isna","https://www.isna.ir/rss","class/item-body content-full-news");
    ArrayList<HashMap<String, String>> rssDataMapArray = testSite.getRssData();
    Assert.assertEquals(30,rssDataMapArray.size());
  }

  @Test
  public void getRssDataWithWrongRssTest() {
    Site testSite = new Site("isna","https://www.isna.ir/rs","class/item-body content-full-news");
    testSite.getRssData();
    Assert.assertEquals("filed on Parsing xml, check your network connection.\n",errContent.toString());
  }

  @Test
  public void addNewsWithWrongRssTest() {
    Site testSite = new Site("isna","https://www.isna.ir/rs","class/item-body content-full-news");
    testSite.addNews();
    Assert.assertEquals("filed on Parsing xml, check your network connection.\n",errContent.toString());
  }

  @After
  public void restoreStream() {
    System.setErr(System.err);
  }
}