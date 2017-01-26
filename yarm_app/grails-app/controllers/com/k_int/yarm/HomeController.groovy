package com.k_int.yarm

import grails.plugin.springsecurity.annotation.Secured

import com.k_int.yarm.PartyRelationship


class HomeController {

  def springSecurityService

  def index() { 

    def user = springSecurityService.currentUser
    def result=[:]

    log.debug("Home::Index");
    if (springSecurityService.isLoggedIn()){
      result.user_contexts = []
      result.user_contexts.add([
                                name:springSecurityService.currentUser.uriName,
                                type:'user',
                                label:springSecurityService.currentUser.displayName]);

      // def user_orgs = PartyRelationship.executeQuery('select pr from PartyRelationship as pr');
      def user_orgs = PartyRelationship.executeQuery('select pr from PartyRelationship as pr where pr.from=:user and pr.role.owner.desc=:cat and pr.status.value=:approved',[user:user, cat:'relationshipRole', approved:'Approved']);
      user_orgs.each { uo ->
        result.user_contexts.add([type:'org', name:uo.to.uriName, label: uo.to.displayName, role: uo.role.value]);
      }


      render(view:'loggedInIndex', model:result)
    }
    else {
      render(view:'index')
    }
  }

  @Secured(['ROLE_USER', 'IS_AUTHENTICATED_FULLY'])
  def login() {
    log.debug("HomeController::login");
    redirect action:'index'
  }

}
