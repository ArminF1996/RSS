package ir.sahab.nimbo2.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseUpdateServiceTest {

  @Test
  public void getInstanceNullReturnTest() {
    Assert.assertNotEquals(null, DatabaseUpdateService.getInstance());
  }

  @Test
  public void setWaitTimeoutNegativeNumberTest() {
    DatabaseUpdateService.getInstance().setWaitTimeout(-5000);
    Assert.assertEquals(30000, DatabaseUpdateService.getInstance().getWaitTimeout());
  }

  @Test
  public void setWaitTimeoutSmallNumberTest() {
    DatabaseUpdateService.getInstance().setWaitTimeout(500);
    Assert.assertEquals(30000, DatabaseUpdateService.getInstance().getWaitTimeout());
  }

  @Test
  public void setNumberOfThreadsInPoolNegativeNumberTest() {
    DatabaseUpdateService.getInstance().setNumberOfThreadsInPool(-4);
    Assert.assertEquals(20, DatabaseUpdateService.getInstance().getNumberOfThreadsInPool());
  }

  @Test
  public void getLOCK_FOR_WAIT_AND_NOTIFY_UPDATE() {}

  @Test
  public void addSiteForUpdate() {}

  @Test
  public void getThreadPoolForUpdaters() {}

  @Test
  public void run() {}
}
