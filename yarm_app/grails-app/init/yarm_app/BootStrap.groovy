package yarm_app

import com.k_int.yarm.auth.User
import com.k_int.yarm.auth.Role
import com.k_int.yarm.auth.UserRole
import com.k_int.yarm.RefdataCategory

class BootStrap {

  def sessionFactory

  def sysusers = [
    [
      name:'admin',
      uriName:'admin',
      pass: System.getenv('YARM_ADM_PW')?:'ChangeMeImmediately',
      display:'Admin',
      email:'ianibbo@gmail.com', 
      roles:['ROLE_ADMIN','ROLE_USER']
    ]
  ]


  def init = { servletContext ->
    log.debug("BootStrap::init");
    setUpUserAccounts()
    setUpRefdata()
  }

  def setUpUserAccounts() {
    sysusers.each { su ->
      log.debug("test ${su.name} ${su.pass} ${su.display} ${su.roles}");
      def user = User.findByUsername(su.name)
      if ( user ) {
        if ( user.password != su.pass ) {
          log.debug("Hard change of user password from config ${user.password} -> ${su.pass}");
          user.password = su.pass;
          user.save(failOnError: true)
        }
        else {
          log.debug("${su.name} present and correct");
        }
      }
      else {
        log.debug("Create user...");
        user = new User(
                      username: su.name,
                      password: su.pass,
                      uriName: su.uriName,
                      displayName: su.display,
                      email: su.email,
                      enabled: true).save(failOnError: true)
      }

      log.debug("Add roles for ${su.name} (${su.roles})");
      su.roles.each { r ->

        def role = Role.findByAuthority(r) ?: new Role(authority:r).save(flush:true, failOnError:true)

        if ( ! ( user.authorities.contains(role) ) ) {
          log.debug("  -> adding role ${role} (${r})");
          UserRole.create user, role
        }
        else {
          log.debug("  -> ${role} already present");
        }
      }
    }
  }

  def setUpRefdata() {
     RefdataCategory.lookupOrCreate('relationshipRole','UOAdmin')
     RefdataCategory.lookupOrCreate('relationshipRole','UOMember')
     RefdataCategory.lookupOrCreate('relationshipRole','UOReadOnly')
     RefdataCategory.lookupOrCreate('relationshipStatus','Pending')
     RefdataCategory.lookupOrCreate('relationshipStatus','Approved')
     RefdataCategory.lookupOrCreate('relationshipStatus','Rejected')
  }

  def destroy = {
  }
}
