package com.k_int.yarm

class BootStrap {

    def init = { servletContext ->

      def user_role = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)

      log.debug("Create admin user...");
      def adminUser = User.findByUsername('admin')
      if ( ! adminUser ) {
        adminUser = new User(
            username: 'admin',
            password: 'admin',
            display: 'Admin',
            email: 'admin@localhost',
            enabled: true).save(failOnError: true)
      }

      // Make sure admin user has all the system roles.
      [user_role].each { role ->
        if (!adminUser.authorities.contains(role)) {
          UserRole.create adminUser, role
        }
      }



    }
    def destroy = {
    }
}
