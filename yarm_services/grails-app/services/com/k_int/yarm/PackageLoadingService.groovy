package com.k_int.yarm


public class PackageLoadingService {

  def running = false;

  @javax.annotation.PostConstruct
  def init() {
    log.debug("PackageLoadingService::init");
  }

  public void triggerSync() {
    sychronized(this) {
      if ( running == false ) {
        running = true;
        runSync()
      }
    }
  }

  public void runSync() {
    log.debug("PackageLoadingService::runSync");

    running = false;
  }

}
