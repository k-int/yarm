package yarm_app

class YarmTagLib {

  static defaultEncodeAs = [taglib:'none']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def yarmTextField = { attrs, body ->
    out << render(template: "simpleTextField", model: attrs, contextPath: '/templates')
  }

  def yarmEmbeddedSearch = { attrs, body ->
    if ( request.dt_configs == null ) {
      request.dt_configs=[:]
    }
    request.dt_configs[attrs.config]=grailsApplication.config.srch_cfg[attrs.config];
    out << render(template: "embeddedSearch", model: attrs, contextPath: '/templates')
  }
}
