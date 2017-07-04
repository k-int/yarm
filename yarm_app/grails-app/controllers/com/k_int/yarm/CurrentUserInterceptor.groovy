package yarm_app

import implicitauth.Tenant


class CurrentUserInterceptor {

  def springSecurityService

  public CurrentUserInterceptor() {
    matchAll();
  }

  boolean before() { 

    // if (!springSecurityService.principal.email){
    //     flash.message = "Please enter your email before you can proceed"
    //     response.sendRedirect("${grailsLinkGenerator.link(controller:'user',action:'userProfile')}")
    //     return false
    // }else {
    //     return true
    // }

    // if (springSecurityService.isLoggedIn()){
    //   request.setAttribute("current_user", springSecurityService.currentUser);
    // }

    // if ( params.institution_shortcode ) {
    //   log.debug("Detected institution shortcode ${params.institution_shortcode} - setting request.institution");
    //   request.institution = Tenant.findByUriName(params.institution_shortcode)
    // }

    true 
  }

  boolean after() { 
    true 
  }

  void afterView() {
    // no-op
  }
}
