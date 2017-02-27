package com.k_int.yarm

import org.grails.core.DefaultGrailsDomainClass

class YarmConfigService {

    def grailsApplication

    def getQueryConfig(String code) {
      def result = grailsApplication.config.srch_cfg[code]
      log.debug("get query config ${code} returns ${result}");
      result
    }
}
