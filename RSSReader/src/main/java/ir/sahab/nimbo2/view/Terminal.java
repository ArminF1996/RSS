package ir.sahab.nimbo2.view;

import ir.sahab.nimbo2.Controller.Controller;
import ir.sahab.nimbo2.model.DatabaseManager;
import ir.sahab.nimbo2.model.DatabaseUpdateService;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Terminal {

  private Scanner reader;
  private static Terminal ourInstance = new Terminal();

  public static Terminal getInstance() {
    return ourInstance;
  }


  private Terminal() {
    reader = new Scanner(System.in);
    DatabaseManager.getInstance();
    this.start();
  }

  private void start() {
      DatabaseUpdateService.getInstance().setNumberOfThreadsInPool(5);
      DatabaseUpdateService.getInstance().setWaitTimeout(30000);
      Thread updateThread = new Thread(DatabaseUpdateService.getInstance());
      updateThread.start();
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
          Controller.getInstance().update();
          break;
        default:
          System.out.println("input is not valid, please try again.\n");
          break;
      }
    }
    DatabaseUpdateService.getInstance().getThreadPoolForUpdaters().shutdownNow();
    updateThread.interrupt();
  }

  private void addSite() {
    System.out.println("Write URL of RSS page.\n");
    String rssUrl = reader.next().toLowerCase();

    System.out.println("choose a name for this URL.\n");
    String siteName = reader.next().toLowerCase();

    System.out.println("enter the site config (like \"class/body\").\n");
    reader.nextLine();
    String siteConfig = reader.nextLine().toLowerCase();

    Controller.getInstance().addSite(rssUrl, siteName, siteConfig);
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
    ResultSet resultSet = null;
    try {
      resultSet = Controller.getInstance().getTenLatestNews(id);
    } catch (SQLException e) {
      System.err.println("some error happen from connecting to database, check the logfile!");
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      System.err.println("latest news not found!");
    }
  }

  private void history() {
    showSitesWithId();
    System.out.println("write site id.");
    int id = reader.nextInt();
    //TODO
  }

  private void today() {
    //TODO
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
    ResultSet resultSet = null;
    try {
      resultSet = Controller.getInstance().findNewsByBody(input);
    } catch (SQLException e) {
      System.err.println("some error happen from connecting to database, check the logfile!");
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      System.err.println("not found any news with searchByBody!");
    }
  }

  private void searchByTitle() {
    System.out.println("write something.");
    reader.nextLine();
    String input = reader.nextLine().toLowerCase();
    ResultSet resultSet = null;
    try {
      resultSet = Controller.getInstance().findNewsByTitle(input);
    } catch (SQLException e) {
      System.err.println("some error happen from connecting to database, check the logfile!");
      return;
    }

    try {
      if (resultSet != null) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("link"));
        }
      }
    } catch (SQLException e) {
      System.err.println("not found any news with searchByTitle!");
    }
  }

  private boolean showSitesWithId() {
    ResultSet searchResult = null;
    try {
      searchResult = Controller.getInstance().getSitesWithId();
    } catch (SQLException e) {
      System.err.println("can not show the list of sites right now, please try again later.");
      return false;
    }
    if (searchResult == null) {
      System.out.println("the list of sites is empty.");
      return false;
    }
    try {
      while (searchResult.next()) {
        System.out.println(
            searchResult.getInt("siteID") + ":" + searchResult.getString("siteName"));
      }
    } catch (SQLException e) {
      System.err.println("can not show the list of sites right now, please try again later.");
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    Terminal terminal = Terminal.getInstance();
  }
}
