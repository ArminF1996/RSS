package ir.sahab.nimbo2.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class SiteRepositoryTest {
  ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setUpStream() {
    System.setErr(new PrintStream(errContent));
  }

  @Test
  public void getInstanceNullReturnTest() {
    Assert.assertNotEquals(null, SiteRepository.getInstance());
  }

  @Test
  public void addSitesToDatabaseWithCorrectInputTest() throws SQLException {
    Site testSite = spy(new Site("isna","https://www.isna.ir/rss","class/item-body content-full-news"));
    SiteRepository.getInstance().addSitesToDatabase(DatabaseManager.getInstance().getConnection(),testSite);
    ResultSet resultSet = SiteRepository.getInstance().showAllSites(DatabaseManager.getInstance().getConnection());
    boolean siteAdded = false;
    while (resultSet.next()) {
      if(resultSet.getString("rssUrl").equals(testSite.getRssUrl())){
        siteAdded = true;
      }
    }
    Assert.assertEquals(true,siteAdded);
  }

  @Test
  public void addSitesToDatabaseWithCorrectInputDuplicateTest() throws SQLException {
    Site testSite = spy(new Site("isna","https://www.isna.ir/rss","class/item-body content-full-news"));
    SiteRepository.getInstance().addSitesToDatabase(DatabaseManager.getInstance().getConnection(),testSite);
    SiteRepository.getInstance().addSitesToDatabase(DatabaseManager.getInstance().getConnection(),testSite);
    ResultSet resultSet = SiteRepository.getInstance().showAllSites(DatabaseManager.getInstance().getConnection());
    int siteAdded = 0;
    while (resultSet.next()) {
      if(resultSet.getString("rssUrl").equals(testSite.getRssUrl())){
        siteAdded++;
      }
    }
    Assert.assertEquals(1,siteAdded);
  }

  @Test
  public void updateConfigOfSite() {}

  @Test
  public void showAllSites() {}

  @Test
  public void findAndSetSiteIDFromDatabase() {}

  @After
  public void restoreStream() {
    System.setErr(System.err);
  }
}