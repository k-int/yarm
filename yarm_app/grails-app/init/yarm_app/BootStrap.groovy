package yarm_app

class BootStrap {

  def init = { servletContext ->
    log.debug("BootStrap::init");
  }

  def destroy = {
  }
}
