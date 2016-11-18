package com.k_int.yarm

class NewTenantWatcherJob {

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

  def execute() {
  }

}
