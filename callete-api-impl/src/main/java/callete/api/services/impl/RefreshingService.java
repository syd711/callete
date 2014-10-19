package callete.api.services.impl;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Abstract superclass for services that regularly refresh their data to send them to the UI.
 */
public abstract class RefreshingService {
  private static final Logger LOG = LoggerFactory.getLogger(RefreshingService.class);
  private long refreshInterval;
  private RefreshThread refreshThread;

  public RefreshingService(long intervalMillis) {
    this.refreshInterval = intervalMillis;
    this.refreshThread = new RefreshThread();
    this.refreshThread.start();
  }

  @SuppressWarnings("unused")
  public void forceRefresh() {
    refreshServiceData();
  }

  public void setRefreshInterval(long interval) {
    this.refreshInterval = interval;
  }

  /**
   * Internal refresh thread for triggering the data update.
   */
  class RefreshThread extends Thread {
    private boolean running = true;

    @Override
    public void run() {
      while (running) {
        try {
          Thread.sleep(refreshInterval);
          refreshServiceData();
        } catch (InterruptedException e) {
          LOG.error("Error in timer thread: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Method to be implemented by subclasses.
   * The concrete implementation will contain the logic to reload the actual service data.
   */
  protected abstract void refreshServiceData();
}
