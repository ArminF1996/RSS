package ir.sahab.nimbo2.view;

import ir.sahab.nimbo2.Controller.Controller;
import ir.sahab.nimbo2.model.DatabaseManager;
import ir.sahab.nimbo2.model.DatabaseUpdateService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Terminal {

  private Scanner reader;
  final static Logger logger = Logger.getLogger(Terminal.class);
  private static Terminal ourInstance = new Terminal();

  public static Terminal getInstance() {
    return ourInstance;
  }

  private Terminal() {
    reader = new Scanner(System.in);
    DatabaseManager.getInstance();
    PropertyConfigurator.configure("log4j.properties");
    this.start();
  }

  private void start() {
    Properties configFile = new Properties();
    InputStream fileInput = null;
    try {
      fileInput = new FileInputStream("config.properties");
      configFile.load(fileInput);
      DatabaseUpdateService.getInstance()
          .setNumberOfThreadsInPool(
              Integer.parseInt(configFile.getProperty("numberOfThreadsInPoolForUpdate")));
      DatabaseUpdateService.getInstance()
          .setWaitTimeout(Integer.parseInt(configFile.getProperty("AutoUpdateWaitTimeInMillis")));
      for (int i = 1; i <= Integer.parseInt(configFile.getProperty("DefaultSiteNumber")); i++) {
        String rssUrl = configFile.getProperty("Site" + i + "RssUrl").toLowerCase();
        String siteName = configFile.getProperty("Site" + i + "Name").toLowerCase();
        String siteConfig = configFile.getProperty("Site" + i + "TextConfig").toLowerCase();
        Controller.getInstance().addSite(rssUrl, siteName, siteConfig);
      }
    } catch (IOException | SQLException ex) {
      System.err.println("some error happen, check the log file.");
      logger.error("currently we can not add sites to database, please check the configFile.", ex);
    } finally {
      if (fileInput != null) {
        try {
          fileInput.close();
          Controller.getInstance().addExistingSitesToUpdateService();
        } catch (SQLException | IOException e) {
          System.err.println("some error happen, check the log file.");
          logger.error("some error happen from connecting to database, check the logfile!", e);
        }
      }
    }
    Thread updateThread = new Thread(DatabaseUpdateService.getInstance());
    updateThread.start();
    boolean flag = true;

    while (flag) {
      System.out.println(
          "Write \"Exit\" to close program.\n"
              + "write \"add\" to add site.\n"
              + "write \"remove\" to remove site.\n"
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
        case "remove":
          removeSite();
          break;
        case "view":
          viewMode();
          break;
        case "update":
          Controller.getInstance().update();
          break;
        default:
          System.out.println("input is not valid, please try again.\n");
          break;
      }
    }
    DatabaseUpdateService.getInstance().getThreadPoolForUpdaters().shutdownNow();
    updateThread.interrupt();
    System.exit(0);
  }

  private void addSite() {
    System.out.println("Write URL of RSS page.\n");
    String rssUrl = reader.next().toLowerCase();

    System.out.println("choose a name for this URL.\n");
    String siteName = reader.next().toLowerCase();

    System.out.println("enter the site config (like \"class/body\").\n");
    reader.nextLine();
    String siteConfig = reader.nextLine().toLowerCase();

    try {
      System.err.println(Controller.getInstance().addSite(rssUrl, siteName, siteConfig));
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error(
          "currently we can not add sites to database, please check log file.", e);
    }
  }

  private void removeSite() {
    if (!showSitesWithId()) {
      return;
    }
    System.out.println("write site id.");
    int id = reader.nextInt();
    try {
      Controller.getInstance().removeSite(id);
      Controller.getInstance().addExistingSitesToUpdateService();
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error(
          "failed on deleting this site from database, please check the database connection.", e);
    }
  }

  private void viewMode() {
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

  private void latest() {
    if (!showSitesWithId()) {
      return;
    }
    System.out.println("write site id.");
    int id = reader.nextInt();
    ResultSet resultSet;
    try {
      resultSet = Controller.getInstance().getTenLatestNews(id);
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error("some error happen from connecting to database, check the database connection!",
          e);
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.warn("latest news not found!", e);
    }
  }

  private void history() {
    if (!showSitesWithId()) {
      return;
    }
    System.out.println("write site id.");
    int id = reader.nextInt();
    System.out.println("write date. be like : 1990-05-22");
    reader.nextLine();
    String date = reader.nextLine();
    int newsNumber;
    try {
      newsNumber = Controller.getInstance().getHistory(id, date);
    } catch (SQLException e) {
      newsNumber = 0;
      System.err.println("some error happen, check the log file.");
      logger.warn("some error happen from database connection.", e);
    }
    System.out.println(newsNumber);
  }

  private void today() {
    ArrayList<HashMap> sitesInformation;
    try {
      sitesInformation = Controller.getInstance().getTodayInformation();
    } catch (SQLException | NullPointerException e) {
      sitesInformation = new ArrayList<>();
      System.err.println("some error happen, check the log file.");
      logger.error("some error happen from database connection.", e);
    }
    for (HashMap siteInfo : sitesInformation) {
      System.out.println(siteInfo.get("siteID") + "  " + siteInfo.get("siteName") + "  " + siteInfo
          .get("numOfNews"));
    }
  }

  private void searchMode() {
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
          System.err.println("input is not valid.\nplease try again.\n");
          break;
      }
    }
  }

  private void searchByBody() {
    System.out.println("write something.");
    reader.nextLine();
    String input = reader.nextLine().toLowerCase();
    ResultSet resultSet;
    try {
      resultSet = Controller.getInstance().findNewsByBody(input);
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error("some error happen from connecting to database, check the database connection!",
          e);
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error("not found any news with searchByBody!", e);
    }
  }

  private void searchByTitle() {
    System.out.println("write something.");
    reader.nextLine();
    String input = reader.nextLine().toLowerCase();
    ResultSet resultSet;
    try {
      resultSet = Controller.getInstance().findNewsByTitle(input);
    } catch (SQLException e) {
      System.err.println("some error happen, check the log file.");
      logger.error("some error happen from connecting to database!", e);
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      logger.warn("not found any news with searchByTitle!", e);
      System.err.println("some error happen, check the log file.");
    }
  }

  private boolean showSitesWithId() {
    ResultSet searchResult = null;
    try {
      searchResult = Controller.getInstance().getSitesWithId();
    } catch (SQLException e) {
      logger.error("can not show the list of sites right now, please try again later.", e);
      System.err.println("some error happen, check the log file.");
      return false;
    }
    if (searchResult == null) {
      System.out.println("the list of sites is empty.");
      return false;
    }
    int number = 0;
    try {
      while (searchResult.next()) {
        number++;
        System.out.println(
            searchResult.getInt("siteID") + ":" + searchResult.getString("siteName"));
      }
      if (number == 0) {
        System.out.println("no site exist.");
        return false;
      }
    } catch (SQLException | NullPointerException e) {
      logger.error("some error happen to database connection or maybe null pointer ex.", e);
      System.err.println("some error happen, check the log file.");
      return false;
    }
    return true;
  }


  public static void main(String[] args) {
  }
}
