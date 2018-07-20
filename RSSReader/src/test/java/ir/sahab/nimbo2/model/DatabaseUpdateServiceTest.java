package ir.sahab.nimbo2.model;

import org.junit.Assert;
import org.junit.Test;

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
}
