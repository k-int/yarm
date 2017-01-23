package yarm_app


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CurrentUserInterceptor)
class CurrentUserInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test currentUser interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"currentUser")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
