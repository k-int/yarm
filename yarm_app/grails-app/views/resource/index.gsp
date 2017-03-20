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
    var yarm_dt_configs=${request.dt_configs as JSON};
    </g:applyCodec>
  </script>
  <asset:javascript src="resource.js" />
</body>
</html>
