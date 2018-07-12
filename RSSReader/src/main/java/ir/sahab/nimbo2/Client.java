package ir.sahab.nimbo2;

import java.util.Scanner;

final class Client {

  private Scanner reader;
  private String clientName;

  Client(String clientName) {

    this.clientName = clientName;
    reader = new Scanner(System.in);
    this.start();
  }

  public String getClientName() {
    return clientName;
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
          update();
          break;
        default:
          System.out.println("input is not valid.\nplease try again.\n");
          break;
      }
    }
  }

  private void addSite() {
    System.out.println("Write URL of RSS page.\n");
    Scanner reader = new Scanner(System.in);
    String rssUrl = reader.next().toLowerCase();
    // todo add this site to db
  }

  private void update() {
    // todo update db
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
    // todo show sites with id
    System.out.println("write site id.");
    int id = reader.nextInt();
    // todo show latest news
  }

  private void history() {
    // todo show sites with id
    System.out.println("write site id.");
    int id = reader.nextInt();
    // todo show history of all day for site
  }

  private void today() {
    // todo show history of all sites for today
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
          System.out.println("input is not valid.\nplease try again.\n");
          break;
      }
    }
  }

  private void searchByBody() {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    // todo show all news that contain this string
  }

  private void searchByTitle() {
    System.out.println("write something.");
    String input = reader.next().toLowerCase();
    // todo show all news that contain this string in title with
  }
}
