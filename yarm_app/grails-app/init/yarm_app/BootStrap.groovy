package yarm_app

class BootStrap {

  def tenantManagerService

  def init = { servletContext ->
    log.debug("BootStrap::init");
  }

  def destroy = {
  }
}
