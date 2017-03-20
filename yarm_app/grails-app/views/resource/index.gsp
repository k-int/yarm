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
        ${k} : { i=${i} }
      </g:each>
    }
    </g:applyCodec>
  </script>
  <asset:javascript src="resource.js" />
</body>
</html>
