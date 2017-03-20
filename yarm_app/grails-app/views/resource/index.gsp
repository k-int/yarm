<%@ page import="grails.converters.JSON" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>YARM</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <g:set var="dt_configs" value="${[:]}" scope="request"/>
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">
        <form class="form-horizontal" method="POST">

         <g:render template="${params.gsp}" contextPath="." model="${['yrt':yrt]}" />

         <div class="form-group">
           <div class="col-sm-offset-2 col-sm-10">
             <button type="submit" class="btn btn-default">Save</button>
           </div>
         </div>

        </form>
      </div>
    </div>
  </div>
  <script type="text/javascript">
    <g:applyCodec encodeAs="none">
    <g:set var="first" value="true" />
    var yarm_dt_configs={
      <g:each in="${request.dt_configs}" var="k,v" status="i">
        <g:if test="${i>0}">,</g:if>
        "${k}" : { 
          "searching": false,
          "bLengthChange": false,
          "bScrollInfinite": true,
          "bScrollCollapse": true,
          "sScrollY": "200px",
          "bPaginate": false,
          "sAjaxDataProp": "recset",
          "columns": [
            <g:each in="${v.qbeConfig.qbeResults}" var="coldef">
              {
                <g:if test="${coldef.type=='link'}">
                  "render":function ( data, type, row ) {
                      return "<a href='"+row['${coldef.name}'].link+"'>"+row['${coldef.name}'].label+"</a>";
                  },
                </g:if>
                "title": "${coldef.heading}",
                "visible":${coldef.visible?:false},
                "data":"${coldef.name}"
              },
            </g:each>
          ],
          ajax : function(data,callback,settings) {
            console.log("ajax(%o,%o,%o)",data,callback,settings);
            var url = "${createLink(controller:'DBSearch', action:'getSearchResult')}?"+$('#${k}_qryform').serialize();
            console.log("Do callback %s",url);

            $.ajax({
              url     : url,
              type    : 'GET',
              dataType: 'json',
              data    : data,
              success : function( data ) {
                console.log("o",data);
                callback(data);
              },
              error   : function( xhr, err ) {
                console.log("o",err);
              }
            });
          }
        }
      </g:each>
    }
    </g:applyCodec>
  </script>
  <asset:javascript src="resource.js" />
</body>
</html>
