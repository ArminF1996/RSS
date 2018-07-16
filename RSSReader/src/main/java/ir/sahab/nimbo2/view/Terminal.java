package ir.sahab.nimbo2.view;

import ir.sahab.nimbo2.Controller.Controller;
import java.sql.SQLException;
import java.util.Scanner;

public class Terminal {

  private Scanner reader;
  private static Terminal ourInstance = new Terminal();

  public static Terminal getInstance() {
    return ourInstance;
  }


  private Terminal() {
    reader = new Scanner(System.in);
    this.start();
  }

  private void start() {
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
          String result = Controller.getInstance().update();
          System.out.println(result);
          break;
        default:
          System.out.println("input is not valid, please try again.\n");
          break;
      }
    }
  }

  private void addSite() {
    System.out.println("Write URL of RSS page.\n");
    String rssUrl = reader.next().toLowerCase();

    System.out.println("choose a name for this URL.\n");
    String siteName = reader.next().toLowerCase();

    System.out.println("enter the site config (like \"class/body\").\n");
    reader.nextLine();
    String siteConfig = reader.nextLine().toLowerCase();

    String result = Controller.getInstance().addSite(rssUrl, siteName, siteConfig);
    System.out.println(result);
  }

  private void viewMode() {
//TODO
//    boolean flag = true;
//
//    while (flag) {
//      System.out.println(
//          "Write \"latest\" to see 10 latest news.\n"
//              + "write \"search\" to go to search mode.\n"
//              + "write \"history\" to see history of sites.\n"
//              + "write \"today\" to see number of news for today for each site.\n"
//              + "write \"back\" to back to start state.");
//
//      String input = reader.next().toLowerCase();
//
//      switch (input) {
//        case "latest":
//          latest();
//          break;
//        case "search":
//          searchMode();
//          break;
//        case "history":
//          history();
//          break;
//        case "today":
//          today();
//          break;
//        case "back":
//          flag = false;
//          break;
//        default:
//          System.out.println("input is not valid.\nplease try again.\n");
//          break;
//      }
//    }
  }

  public static void main(String[] args) {
    Terminal terminal = Terminal.getInstance();
  }
}
