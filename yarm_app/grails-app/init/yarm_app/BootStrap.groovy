package yarm_app

class BootStrap {

  def tenantManagerService

  def init = { servletContext ->
    log.debug("BootStrap::init");

    // Make sure we at least have a demo tenant
    // N.B. Developers - add something like this to /etc/hosts so you can visit http://demo.localdomain/ and get access to the demo tenant
    //   127.0.0.1       localhost pghost demo.localdomain
    def demo_tenant = com.k_int.yarm.Tenant.findByUriname('demo') ?: new com.k_int.yarm.Tenant(uriname:'demo').save(flush:true, failOnError:true);

    log.debug("set up tenants");
    tenantManagerService.getLatestTenants()
  }

  def destroy = {
  }
}
