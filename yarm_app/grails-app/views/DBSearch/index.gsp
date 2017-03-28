<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>YARM</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <script language="JavaScript">
      var yarm_dt_config = {
        "searching": false,
        "bLengthChange": false,
        "bScrollInfinite": true,
        "bScrollCollapse": true,
        "sScrollY": "200px",
        "bPaginate": true,
        "pageLength": 50,
        "sAjaxDataProp": "recset",
        "columns": [
          <g:each in="${qryconfig.qbeConfig.qbeResults}" var="coldef">
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
          var url = "${createLink(action:'getSearchResult')}?"+$('#__dbsearchForm').serialize();
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
      };
    </script>
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">

        <div class="panel-body">
          <g:render template="qbeform" contextPath="." model="${[formdefn:qryconfig.qbeConfig?.qbeForm, 'hide':[], cfg:qryconfig.qbeConfig]}" />
        </div>

        <table id="yarmQResTable" 
               data-qryurl="${createLink(action:'getSearchResult',params:[srch_cfg:params.srch_cfg])}"
               class="table-striped">
          <thead>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <asset:javascript src="search.js" />
</body>
</html>
