package ir.sahab.nimbo2.model;

import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class NewsRepositoryTest {

  @Test
  public void getInstanceNullReturnTest() {
    Assert.assertNotEquals(null, NewsRepository.getInstance());
  }

  @Test
  public void addNewsToDatabaseTest() throws SQLException {
    News newsMock =
        spy(
            new News(
                1,
                "سد «کوکبیه» شازند قربانی گرفت",
                "2018-07-20 14:33:14",
                "https://www.isna.ir/news/97042915819/سد-کوکبیه-شازند-قربانی-گرفت"));
    doReturn("https://www.isna.ir/news/97042915819/سد-کوکبیه-شازند-قربانی-گرفت")
        .when(newsMock)
        .getLink();
    doReturn("سد «کوکبیه» شازند قربانی گرفت").when(newsMock).getTitle();
    doReturn(1).when(newsMock).getSiteID();
    doReturn(
            "سد کوکبیه شازند امروز قربانی گرفت. به گزارش ایسنا، و به نقل از آتشنشانی اراک، جوان 23 سالهای که برای شنا به این سد رفته بود به کام مرگ فرو رفت. براساس این گزارش، غواصان آتشنشانی اراک با همکاری آتشنشانان شازند جسد وی را از آب بیرون کشیدند. انتهای پیام لینک کوتاه")
        .when(newsMock)
        .getBody();
    NewsRepository.getInstance().addNewsToDatabase(newsMock);
    ResultSet resultSet =
        NewsRepository.getInstance()
            .searchByTitle(
                DatabaseManager.getInstance().getConnection(), "سد «کوکبیه» شازند قربانی گرفت");
    resultSet.last();
    Assert.assertEquals(1,resultSet.getRow());
    resultSet.first();
    Assert.assertEquals("https://www.isna.ir/news/97042915819/سد-کوکبیه-شازند-قربانی-گرفت",resultSet.getString("link"));
  }

  @Test
  public void getTenNewestNewsOfSite() {}

  @Test
  public void getConfig() {}

  @Test
  public void getNumberOfNewsHistoryForDate() {}
}
