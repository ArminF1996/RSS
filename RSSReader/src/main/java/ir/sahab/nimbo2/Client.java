package ir.sahab.nimbo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

final class Client {

    private Scanner reader;
    private String clientName;
    private RssData rssData;

    /**
     * this is constructor of Client class.
     *
     * @param clientName each client must have a name.
     */
    Client(String clientName) {

        this.clientName = clientName;
        reader = new Scanner(System.in);
        rssData = new RssData();
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

    /**
     * with this method,the clients can adding new sites to application.
     */
    private void addSite() {
        System.out.println("Write URL of RSS page.\n");
        String rssUrl = reader.next().toLowerCase();

        System.out.println("choose a name for this URL.\n");
        String siteName = reader.next().toLowerCase();

        // todo get site's config

        DbConnector.addSite(rssUrl, siteName, "");
        ArrayList<HashMap<String, String>> rssDataHMap = rssData.getRssData(rssUrl);



        //todo add RSS data to db

    }

    /**
     * with this method, the clients can updating the database.
     */
    private void update() {
        // todo update db
    }

    /**
     * this method create for handling the state of client activity. you can see the state diagram for
     * client activity for more info.
     */
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

    /**
     * with this method, the clients can see the 10 latest news from one news agency.
     */
    private void latest() {
        // todo show sites with id
        System.out.println("write site id.");
        int id = reader.nextInt();
        // todo show latest news
    }

    /**
     * with this method, the clients can see the history of number of published news per day for
     * specified news agency.
     */
    private void history() {
        // todo show sites with id
        System.out.println("write site id.");
        int id = reader.nextInt();
        // todo show history of all day for site
    }

    /**
     * with this method, the clients can see the number of published news for today for each news
     * agency.
     */
    private void today() {
        // todo show history of all sites for today
    }

    /**
     * this method create for handling the state of client activity. you can see the state diagram for
     * client activity for more info.
     */
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

    /**
     * with this method, the clients can search the news by some string in the news body.
     */
    private void searchByBody() {
        System.out.println("write something.");
        String input = reader.next().toLowerCase();
        // todo show all news that contain this string
    }

    /**
     * with this method, the clients can search the news by some string in the news title.
     */
    private void searchByTitle() {
        System.out.println("write something.");
        String input = reader.next().toLowerCase();
        // todo show all news that contain this string in title with
    }
}
