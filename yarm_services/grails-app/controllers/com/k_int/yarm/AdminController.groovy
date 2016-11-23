package com.k_int.yarm

class AdminController {

  def packageLoadingService

  def index() { }

  def triggerRemoteKBSync() {
    log.debug("AdminController::triggerRemoteKBSync");
    packageLoadingService.triggerSync()
  }
}
