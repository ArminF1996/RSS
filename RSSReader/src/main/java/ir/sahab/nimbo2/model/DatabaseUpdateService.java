package ir.sahab.nimbo2.model;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseUpdateService implements Runnable {

  private static DatabaseUpdateService ourInstance = new DatabaseUpdateService();
  private ExecutorService threadPoolForUpdaters;
  private int waitTimeout;
  private ArrayList<Site> sitesInDatabase;
  private final Object LOCK_FOR_WAIT_AND_NOTIFY_UPDATE;

  public static DatabaseUpdateService getInstance() {
    return ourInstance;
  }

  public void setWaitTimeout(int waitTimeout) {
    this.waitTimeout = waitTimeout;
  }

  public void setNumberOfThreadsInPool(int numberOfThreadsInPool) {
    threadPoolForUpdaters = Executors.newFixedThreadPool(numberOfThreadsInPool);
  }

  public Object getLOCK_FOR_WAIT_AND_NOTIFY_UPDATE() {
    return LOCK_FOR_WAIT_AND_NOTIFY_UPDATE;
  }

  private DatabaseUpdateService() {
    sitesInDatabase = new ArrayList<>();
    LOCK_FOR_WAIT_AND_NOTIFY_UPDATE = new Object();
  }

  void addSiteForUpdate(Site newSite) {
    sitesInDatabase.add(newSite);
  }

  public ExecutorService getThreadPoolForUpdaters() {
    return threadPoolForUpdaters;
  }

  @Override
  public void run() {
    while (true) {
      for (int i = 0; i < sitesInDatabase.size(); i++) {
        threadPoolForUpdaters.submit(new UpdateSite(i));
      }
      synchronized (LOCK_FOR_WAIT_AND_NOTIFY_UPDATE) {
        try {
          LOCK_FOR_WAIT_AND_NOTIFY_UPDATE.wait(waitTimeout);
        } catch (InterruptedException e) {
          break;
        }
      }
    }
  }

  private class UpdateSite implements Runnable {
    int sitePlaceInArray;

    UpdateSite(int sitePlaceInArray) {
      this.sitePlaceInArray = sitePlaceInArray;
    }

    @Override
    public void run() {
      sitesInDatabase.get(sitePlaceInArray).addNews();
    }
  }
}
