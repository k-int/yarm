package yarm_app

class BootStrap {

  def init = { servletContext ->
    println("\n\nstart\n\n");
    log.debug("BootStrap::init");
  }

  def destroy = {
  }
}
