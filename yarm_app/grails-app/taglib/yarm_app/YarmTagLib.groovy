package yarm_app

class YarmTagLib {
    static defaultEncodeAs = [taglib:'none']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def yarmTextField = { attrs, body ->
    out << render(template: "simpleTextField", model: attrs, contextPath: '/templates')
  }
}
