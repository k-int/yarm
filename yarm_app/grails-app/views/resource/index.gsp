<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>YARM</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">
        <form class="form-horizontal">

         <g:render template="${params.gsp}" contextPath="." model="${['__yr':__yr]}" />

         <div class="form-group">
           <div class="col-sm-offset-2 col-sm-10">
             <button type="submit" class="btn btn-default">Sign in</button>
           </div>
         </div>

        </form>
      </div>
    </div>
  </div>
</body>
</html>
