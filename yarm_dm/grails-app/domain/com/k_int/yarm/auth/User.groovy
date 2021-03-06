package com.k_int.yarm.auth

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.k_int.yarm.Party
import com.k_int.yarm.Tenant

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User extends Party implements Serializable {

  private static final long serialVersionUID = 1

  transient springSecurityService

  String username
  String password
  String email
  boolean enabled = true
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  Tenant homeInstitution

  Set<Role> getAuthorities() {
    UserRole.findAllByUser(this)*.role
  }

  def beforeInsert() {
    encodePassword()
  }

  def beforeUpdate() {
    if (isDirty('password')) {
      encodePassword()
    }
  }

  protected void encodePassword() {
    password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
  }

  static transients = ['springSecurityService']

  static constraints = {
    password blank: false, password: true
    username blank: false, unique: true
    email blank: false, nullable:true
    homeInstitution blank: false, nullable:true
    accountLocked blank: false, nullable:true
    accountExpired blank: false, nullable:true
    passwordExpired blank: false, nullable:true
  }

  static mapping = {
    table 'yarm_user'
    password column: '`password`'
    homeInstitution column:'home_inst_fk'
  }

  static def refdataFind(params) {
    def result = [];
    def ql = null;

    def query = "from User as u where lower(u.username) like :q or lower(u.displayName) like :q or lower(u.email) like :q"
    def query_params = [q:"%${params.q.toLowerCase()}%"]

    ql = User.findAll(query, query_params, params)

    if ( ql ) {
      ql.each { id ->
        switch(params.mode) {
          case 'stdid':
            result.add([id:id.id,text:"${id.username} / ${id.displayName?:''}"])
            break;
          default:
            result.add([id:"${id.class.name}:${id.id}",text:"${id.username} / ${id.displayName?:''}"])
            break;
        }
      }
    }

    result
  }

  public String toString() {
    return "${username} / ${displayName?:'No display name'}".toString();
  }


}
