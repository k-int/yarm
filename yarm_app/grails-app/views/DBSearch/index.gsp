<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>YARM</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <asset:stylesheet src="datatables.min.css"/>
    <asset:javascript src="gokb/search.js" />
    <script language="JavaScript">
      var yarm_dt_config = {
        "searching": false,
        "bLengthChange": false,
        "bScrollInfinite": true,
        "bScrollCollapse": true,
        "sScrollY": "200px",
        "bPaginate": false,
        "sAjaxDataProp": "recset",
        "columns": [
          { "title": "c1" },
          { "title": "c2" },
          { "title": "c3" }
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

          // callback({ aaData: [ [ "one1", "two", "three", "four", "five" ],
          //                      [ "one2", "two", "three", "four", "five" ] ] } );
          // console.log("Done callback");
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
               class="table table-striped">
          <thead>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</body>
</html>
