package ir.sahab.nimbo2.model;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class DatabaseManagerTest {

  @Test
  public void getInstanceNullReturnTest() {
    Assert.assertNotEquals(null, DatabaseManager.getInstance());
  }

  @Test
  public void getConnectionNullReturnTest() throws SQLException {
    Assert.assertNotEquals(null, DatabaseManager.getInstance().getConnection());
  }

  @Test
  public void getConnectionDatabaseExistTest() throws SQLException {
    Assert.assertEquals("nimroo", DatabaseManager.getInstance().getConnection().getCatalog());
  }
}
