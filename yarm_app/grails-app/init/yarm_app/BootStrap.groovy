package yarm_app

import com.k_int.yarm.GlobalRecordSource
import com.k_int.yarm.RefdataCategory
import implicitauth.User

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.SecurityFilterPosition


class BootStrap {

  def sessionFactory

  def init = { servletContext ->

    log.debug("## YARM APP ## BootStrap::init");
    SpringSecurityUtils.clientRegisterFilter('jwtPreauthFilter', SecurityFilterPosition.PRE_AUTH_FILTER)

    setUpRefdata()
    setUpGlobalSources()

    def num_users = User.executeQuery('select count(u.id) from User as u');

    log.debug("found ${num_users[0]}. This select checks that cross module domain classes are working as expected");
  }

  def setUpRefdata() {
     RefdataCategory.lookupOrCreate('relationshipRole','Administrator',"2")
     RefdataCategory.lookupOrCreate('relationshipRole','Member', "1")
     RefdataCategory.lookupOrCreate('relationshipRole','Read Only User',"3")
     RefdataCategory.lookupOrCreate('relationshipStatus','Pending',"2")
     RefdataCategory.lookupOrCreate('relationshipStatus','Approved',"1")
     RefdataCategory.lookupOrCreate('relationshipStatus','Rejected',"3")
     RefdataCategory.lookupOrCreate('orgType','Institution')
     RefdataCategory.lookupOrCreate('orgType','Consortium')
     RefdataCategory.lookupOrCreate('orgType','Vendor')
     RefdataCategory.lookupOrCreate('orgType','Department')
  }

  def setUpGlobalSources() {
    def gokb_record_source = GlobalRecordSource.findByIdentifier('gokbPackages') ?: new GlobalRecordSource(
                                                                                          identifier:'gokbPackages',
                                                                                          name:'GOKB',
                                                                                          type:'OAI',
                                                                                          haveUpTo:null,
                                                                                          uri:'https://gokb.openlibraryfoundation.org/gokb/oai/packages',
                                                                                          listPrefix:'oai_dc',
                                                                                          fullPrefix:'gokb',
                                                                                          principal:null,
                                                                                          credentials:null,
                                                                                          rectype:0).save(flush:true, stopOnError:true)
    log.debug("Gokb record source: ${gokb_record_source}");
  }

  def destroy = {
  }
}
