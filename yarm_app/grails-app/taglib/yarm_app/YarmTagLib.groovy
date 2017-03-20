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
    request.dt_configs[attrs.config]=[
      'searching':false,
      'bLengthChange': false,
      'bScrollInfinite': true,
      'bScrollCollapse': true,
      'sScrollY': '200px',
      'bPaginate': false,
      'sAjaxDataProp': 'recset'
    ]
      //        "columns": [
      //          <g:each in="${qryconfig.qbeConfig.qbeResults}" var="coldef">
      //            {
      //              <g:if test="${coldef.type=='link'}">
      //                "render":function ( data, type, row ) {
      //                    return "<a href='"+row['${coldef.name}'].link+"'>"+row['${coldef.name}'].label+"</a>";
      //                },
      //              </g:if>
      //              "title": "${coldef.heading}",
      //              "visible":${coldef.visible?:false},
      //              "data":"${coldef.name}"
      //            },
      //          </g:each>
      //        ],
      //        ajax : function(data,callback,settings) {
      //          console.log("ajax(%o,%o,%o)",data,callback,settings);
      //          var url = "${createLink(action:'getSearchResult')}?"+$('#__dbsearchForm').serialize();
      //          console.log("Do callback %s",url);
      //
      //          $.ajax({
      //            url     : url,
      //            type    : 'GET',
      //            dataType: 'json',
      //            data    : data,
      //            success : function( data ) {
      //              console.log("o",data);
      //              callback(data);
      //            },
      //            error   : function( xhr, err ) {
      //              console.log("o",err);
      //            }
      //          });
      //        }
      //
    out << render(template: "embeddedSearch", model: attrs, contextPath: '/templates')
  }
}
