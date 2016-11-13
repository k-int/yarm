package com.k_int.yarm

class NewTenantWatcherJob {

  static Date tenant_cursor = new Date(0);


  static triggers = {
    // Every minute
    cron name:'NewTenantWatcher', cronExpression: "0 0/1 * * * ?"
    // cronExpression: "s m h D M W Y"
    //                  | | | | | | `- Year [optional]
    //                  | | | | | `- Day of Week, 1-7 or SUN-SAT, ?
    //                  | | | | `- Month, 1-12 or JAN-DEC
    //                  | | | `- Day of Month, 1-31, ?
    //                  | | `- Hour, 0-23
    //                  | `- Minute, 0-59
    //                  `- Second, 0-59
  }

  @javax.annotation.PostConstruct
  def init() {
    log.debug("NewTenantWatcher::init()");
  }


  def execute() {
    log.debug("NewTenantWatcher Job");
    Tenant.executeQuery('select t from Tenant as t where t.dateCreated > :d order by t.dateCreated',[d:tenant_cursor]).each {
      log.debug("Adding tenant : ${it}");
      if ( tenant_cursor < it.dateCreated ) {
        tenant_cursor = it.dateCreated
      }
    }
    println("***");
  }

}
