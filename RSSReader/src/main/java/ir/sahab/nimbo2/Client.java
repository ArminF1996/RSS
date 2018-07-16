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

  public Client(String clientName)
      throws SQLException, ParserConfigurationException, SAXException, IOException {
    this.clientName = clientName;
    reader = new Scanner(System.in);
    LOCKFORWAITANDNOTIFY = new Object();
    this.start();
  }

  /**
   * TODO
   * add to Terminal class
   * not added line remain
   */
  private void start(){
    updateService = new UpdateService(dbConnector, LOCKFORWAITANDNOTIFY);
    Thread updateThread = new Thread(updateService);
    updateThread.start();

    updateService.threadPoolForUpdaters.shutdownNow();
    updateThread.interrupt();
  }

  /**
   * TODO
   * add to Terminal class
   * not added line remain
   */
  private void addSite(){
//    updateService.addSiteForUpdate(rssUrl, siteName);
  }

  /**
   * TODO
   * add to Controller class
   * not added line remain
   */
  private void update() {
    synchronized (LOCKFORWAITANDNOTIFY) {
      LOCKFORWAITANDNOTIFY.notify();
    }
  }

  /**
   * TODO
   * add to Terminal class
   * not added line remain
   */
  private void viewMode(){

  }

  /**
   * with this method, the clients can see the 10 latest news from one news agency.
   */
  private void latest() throws SQLException {
    dbConnector.printSitesId();
    System.out.println("write site id.");
    int id = reader.nextInt();
    dbConnector.printLatestNews(id);
  }

  /**
   * with this method, the clients can see the history of number of published news per day for
   * specified news agency.
   */
  private void history() throws SQLException {
    dbConnector.printSitesId();
    System.out.println("write site id.");
    int id = reader.nextInt();
    // TODO show history of all day for site
  }

  /**
   * with this method, the clients can see the number of published news for today for each news
   * agency.
   */
  private void today() {
    dbConnector.printTodayNewsNumberForEachSite();
  }

  /**
   * this method create for handling the state of client activity. you can see the state diagram for
   * client activity for more info.
   */
  private void searchMode() throws SQLException {

    boolean flag = true;

    while (flag) {
      System.out.println(
          "write \"body\" to search by body.\n"
              + "write \"title\" to search by title.\n"
              + "write \"back\" to back to view mode.");
      String input = reader.next().toLowerCase();

      switch (input) {
        case "body":
          searchByBody();
          break;
        case "title":
          searchByTitle();
          break;
        case "back":
          flag = false;
          break;
        default:
          System.out.println("input is not valid.\nplease try again.\n");
          break;
      }
    }
  }

  /**
   * with this method, the clients can search the news by some string in the news body.
   */
  private void searchByBody() throws SQLException {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    dbConnector.searchInBody(input);
  }

  /**
   * with this method, the clients can search the news by some string in the news title.
   */
  private void searchByTitle() throws SQLException {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    dbConnector.searchInTitle(input);
  }
}
