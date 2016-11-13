package yarm_app

class BootStrap {

  def init = { servletContext ->
    log.debug("BootStrap::init");

    def demo_tenant = com.k_int.yarm.Tenant.findByUriname('demo') ?: new com.k_int.yarm.Tenant(uriname:'demo').save(flush:true, failOnError:true);

  }

  def destroy = {
  }
}
