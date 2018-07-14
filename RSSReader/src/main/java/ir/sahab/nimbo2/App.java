package ir.sahab.nimbo2;

import java.sql.SQLException;

public class App {
  public static void main(String[] args) {
    try {
      DbConnector.createEntities();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Client client1 = new Client("console");
  }
}
