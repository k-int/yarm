package com.k_int.yarm

import static grails.gorm.multitenancy.Tenants.*

class HomeController {

  def index() { 
    log.debug("Home::Index");
    withCurrent {
      log.debug("Got current tenant ${grails.gorm.multitenancy.Tenants.currentId}");
    }
  }
}
