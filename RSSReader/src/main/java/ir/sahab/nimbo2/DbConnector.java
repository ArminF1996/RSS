package ir.sahab.nimbo2;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DbConnector {

    private static String dbUrl = "jdbc:mysql://localhost:3306/nimroo?autoReconnect=true&useSSL=true&useUnicode=true&characterEncoding=utf-8";
    private static String userName = "armin";
    private static String passWord = "nimroo";
    private static String createSiteEntity =
            "create table if not exists sites(siteID int PRIMARY KEY AUTO_INCREMENT, siteName TINYTEXT CHARACTER SET utf8, rssUrl TINYTEXT CHARACTER SET utf8, configSettings varchar(100));";
    private static String createHistoryEntity =
            "create table if not exists history(siteID int, date date, numberOfNews int, PRIMARY KEY (siteID,date));";
    private static String createNewsEntity =
            "create table if not exists news(newsID int PRIMARY KEY AUTO_INCREMENT, link TEXT CHARACTER SET utf8, siteID int, title TEXT CHARACTER SET utf8, publishDate date, body MEDIUMTEXT CHARACTER SET utf8);";

    public static void setDbUrl(String dbUrl) {
        DbConnector.dbUrl = dbUrl;
    }

    public static void setUserName(String userName) {
        DbConnector.userName = userName;
    }

    public static void setPassWord(String passWord) {
        DbConnector.passWord = passWord;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getUserName() {
        return userName;
    }

    /**
     * whit this method, we can create database entities. you can see ERD diagram for more
     * information.
     *
     * @return the result of this commands.
     */
    public static Boolean createEntities() {

        Boolean result = true;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, userName, passWord);

            try {
                PreparedStatement siteEntity = connection.prepareStatement(createSiteEntity);
                siteEntity.executeUpdate();
                PreparedStatement historyEntity = connection.prepareStatement(createNewsEntity);
                historyEntity.executeUpdate();
                PreparedStatement newsEntity = connection.prepareStatement(createHistoryEntity);
                newsEntity.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * with this method, the clients can add new site to db.
     *
     * @param rssUrl   url of news agency's rssPage.
     * @param siteName news agency site's name.
     * @param config   config of this site. TODO
     * @return return the result of adding new site.
     */
    public static Boolean addSite(String rssUrl, String siteName, String config) {
        Boolean result = true;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, userName, passWord);
            try {
                PreparedStatement addSite =
                        connection.prepareStatement(
                                "INSERT  INTO sites  (siteName,rssUrl,configSettings) values (?,?,?)");
                addSite.setString(1, siteName);
                addSite.setString(2, rssUrl);
                addSite.setString(3, config);
                addSite.executeUpdate();
            } catch (SQLException e) {
                result = false;
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Boolean addNewsForSite(ArrayList<HashMap<String, String>> hmArr, String siteName) {
        Boolean result = true;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, userName, passWord);
            for (HashMap<String, String> hm: hmArr) {
                try {
                    PreparedStatement insertData = connection.prepareStatement("INSERT  INTO news(link,siteID,title,publishDate,body) values (?,?,?,?,?)");

                    insertData.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }
}
