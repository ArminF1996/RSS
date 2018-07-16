package ir.sahab.nimbo2.model;

import java.sql.ResultSet;

public interface SiteRepository {

  String add();

  String remove();

  String updateNewsOfSite();

  String updateConfigOfSite();

  ResultSet getNumberOfNewsForToday();

  ResultSet getTenNewestNewsOfSite();

  ResultSet getNumberOfNewsHistoryForPreviousDays();

}
