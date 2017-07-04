package yarm_app

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration


// import org.springframework.boot.autoconfigure.transaction.jta.*
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration
// @EnableAutoConfiguration(exclude=[JtaAutoConfiguration])

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
