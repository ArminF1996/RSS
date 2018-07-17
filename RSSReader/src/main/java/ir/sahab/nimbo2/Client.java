package ir.sahab.nimbo2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

final class Client {

  private Scanner reader;
  private String clientName;
  private DataBase dbConnector;
  private final Object LOCKFORWAITANDNOTIFY;
  private UpdateService updateService;

  public Client(String clientName) {
    LOCKFORWAITANDNOTIFY = new Object();
  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void start() {
    updateService = new UpdateService(dbConnector, LOCKFORWAITANDNOTIFY);
    Thread updateThread = new Thread(updateService);
    updateThread.start();

    updateService.threadPoolForUpdaters.shutdownNow();
    updateThread.interrupt();
  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void addSite() {
    updateService.addSiteForUpdate("", "");
  }

  /**
   * TODO add to Controller class not added line remain
   */
  private void update() {
    synchronized (LOCKFORWAITANDNOTIFY) {
      LOCKFORWAITANDNOTIFY.notify();
    }
  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void viewMode() {

  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void latest() {

  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void history() throws SQLException {

  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void today() {
    dbConnector.printTodayNewsNumberForEachSite();
  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void searchMode() throws SQLException {

  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void searchByBody() throws SQLException {
    dbConnector.searchInBody("");
  }

  /**
   * TODO add to Terminal class not added line remain
   */
  private void searchByTitle() throws SQLException {
    dbConnector.searchInTitle("");
  }
}
