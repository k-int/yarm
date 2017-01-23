package yarm_app


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

    if (springSecurityService.isLoggedIn()){
      request.setAttribute("current_user", springSecurityService.currentUser);
    }

    true 
  }

  boolean after() { 
    true 
  }

  void afterView() {
    // no-op
  }
}
