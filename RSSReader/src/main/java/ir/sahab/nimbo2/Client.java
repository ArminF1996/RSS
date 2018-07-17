package ir.sahab.nimbo2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

final class Client {

  private Scanner reader;
  private String clientName;
  private DataBase dbConnector;
  private final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE;
  private UpdateService updateService;

  /**
   * this is constructor of Client class.
   *
   * @param clientName each client must have a name.
   */
  Client(String clientName) throws SQLException {
    this.clientName = clientName;
    reader = new Scanner(System.in);
    LOCK_FOR_WAIT_AND_NOTIFY_UPDATE = new Object();
    this.start();
  }

  /**
   * with this method we can get the client's name.
   *
   * @return clientName.
   */
  public String getClientName() {
    return clientName;
  }

  /**
   * this method create for handling the state of client activity. you can see the state diagram for
   * client activity for more info.
   */
  private void start() throws SQLException {
    Properties configFile = new Properties();
    InputStream fileInput = null;
    String userName;
    String password;
    Thread updateThread = null;
    try {
      fileInput = new FileInputStream("config.properties");
      configFile.load(fileInput);
      userName = configFile.getProperty("DataBaseUserName");
      password = configFile.getProperty("DataBasePassword");
      dbConnector = new DataBase(userName, password);
      dbConnector.createEntities();
      updateService =
          new UpdateService(
              dbConnector,
              LOCK_FOR_WAIT_AND_NOTIFY_UPDATE,
              Integer.parseInt(configFile.getProperty("numberOfThreadsInPoolForUpdate")),
              Integer.parseInt(configFile.getProperty("AutoUpdateWaitTimeInMillis")));
      updateThread = new Thread(updateService);
      updateThread.start();
      for (int i = 1; i <= Integer.parseInt(configFile.getProperty("DefaultSiteNumber")); i++) {
        String rssUrl = configFile.getProperty("Site" + i + "RssUrl").toLowerCase();
        String siteName = configFile.getProperty("Site" + i + "Name").toLowerCase();
        String siteConfig = configFile.getProperty("Site" + i + "TextConfig").toLowerCase();
        dbConnector.addSite(rssUrl, siteName, siteConfig);
        updateService.addSiteForUpdate(rssUrl, siteName);
      }

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    boolean flag = true;

    while (flag) {
      System.out.println(
          "Write \"Exit\" to close program.\n"
              + "write \"add\" to add site.\n"
              + "write \"view\" to go to view mode.\n"
              + "write \"update\" to updating current data.\n");

      String input = reader.next().toLowerCase();

      switch (input) {
        case "exit":
          flag = false;
          break;
        case "add":
          addSite();
          break;
        case "view":
          viewMode();
          break;
        case "update":
          update();
          break;
        default:
          System.out.println("input is not valid.\nplease try again.\n");
          break;
      }
    }
    updateService.threadPoolForUpdaters.shutdownNow();
    updateThread.interrupt();
  }

  /** with this method,the clients can adding new sites to application. */
  private void addSite() throws SQLException {
    System.out.println("Write URL of RSS page.\n");
    String rssUrl = reader.next().toLowerCase();

    System.out.println("choose a name for this URL.\n");
    String siteName = reader.next().toLowerCase();

    System.out.println("enter the site config (like \"class/body\").\n");
    reader.nextLine();
    String siteConfig = reader.nextLine().toLowerCase();

    dbConnector.addSite(rssUrl, siteName, siteConfig);
    updateService.addSiteForUpdate(rssUrl, siteName);
  }

  /** with this method, the clients can updating the database. */
  private void update() {
    synchronized (LOCK_FOR_WAIT_AND_NOTIFY_UPDATE) {
      LOCK_FOR_WAIT_AND_NOTIFY_UPDATE.notify();
    }
  }

  /**
   * this method create for handling the state of client activity. you can see the state diagram for
   * client activity for more info.
   */
  private void viewMode() throws SQLException {

    boolean flag = true;

    while (flag) {
      System.out.println(
          "Write \"latest\" to see 10 latest news.\n"
              + "write \"search\" to go to search mode.\n"
              + "write \"history\" to see history of sites.\n"
              + "write \"today\" to see number of news for today for each site.\n"
              + "write \"back\" to back to start state.");

      String input = reader.next().toLowerCase();

      switch (input) {
        case "latest":
          latest();
          break;
        case "search":
          searchMode();
          break;
        case "history":
          history();
          break;
        case "today":
          today();
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

  /** with this method, the clients can see the 10 latest news from one news agency. */
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
    System.out.println("write date.");
    reader.nextLine();
    String date = reader.nextLine();
    dbConnector.printSiteHistory(id, date);
  }

  /**
   * with this method, the clients can see the number of published news for today for each news
   * agency.
   */
  private void today() throws SQLException {
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

  /** with this method, the clients can search the news by some string in the news body. */
  private void searchByBody() throws SQLException {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    dbConnector.searchInBody(input);
  }

  /** with this method, the clients can search the news by some string in the news title. */
  private void searchByTitle() throws SQLException {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    dbConnector.searchInTitle(input);
  }
}
