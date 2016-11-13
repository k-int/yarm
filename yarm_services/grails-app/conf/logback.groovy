import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

root(WARN, ['STDOUT'])

logger ('grails.app.init', DEBUG)
logger ('grails.app.controllers', DEBUG)
logger ('grails.app.domains', DEBUG)
logger ('grails.app.jobs', DEBUG)
logger ('grails.app.services', DEBUG)
logger ('com.k_int', DEBUG)
logger ('grails.gorm.multitenancy.Tenants', DEBUG)

def targetDir = BuildSettings.TARGET_DIR

if (Environment.isDevelopmentMode() ) {

  if ( targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
  }



  logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)

}
