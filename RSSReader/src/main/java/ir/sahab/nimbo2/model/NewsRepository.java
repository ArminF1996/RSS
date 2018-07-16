package ir.sahab.nimbo2.model;

import java.sql.ResultSet;

public interface NewsRepository {

  void addNews();

  void remove();

  ResultSet searchByTitle();

  ResultSet searchByBody();
}
