package ir.sahab.nimbo2.model;

import org.junit.Assert;
import org.junit.Test;

public class NewsTest {

  @Test
  public void validInputsForConstructor() {
    News testNews =
        new News(
            1,
            "سد «کوکبیه» شازند قربانی گرفت",
            "2018-07-20 14:33:14",
            "https://www.isna.ir/news/97042915819/سد-کوکبیه-شازند-قربانی-گرفت");
    Assert.assertEquals(
        "سد کوکبیه شازند امروز قربانی گرفت. به گزارش ایسنا، و به نقل از آتشنشانی اراک، جوان 23 سالهای که برای شنا به این سد رفته بود به کام مرگ فرو رفت. براساس این گزارش، غواصان آتشنشانی اراک با همکاری آتشنشانان شازند جسد وی را از آب بیرون کشیدند. انتهای پیام لینک کوتاه",
        testNews.getBody());
  }

  @Test
  public void invalidLinkForConstructor() {
    News testNews =
            new News(
                    1,
                    "سد «کوکبیه» شازند قربانی گرفت",
                    "2018-07-20 14:33:14",
                    "https://www.isna.ir/");
    Assert.assertEquals("main body of news not found!",testNews.getBody());
  }

  @Test
  public void NullPublishDateForConstructor() {
    News testNews =
            new News(
                    1,
                    "سد «کوکبیه» شازند قربانی گرفت",
                    null,
                    "https://www.isna.ir/news/97042915819/سد-کوکبیه-شازند-قربانی-گرفت");
    Assert.assertNotEquals(null,testNews.getPublishDate());
  }
}
