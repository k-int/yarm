<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>YARM</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
    <asset:stylesheet src="datatables.min.css"/>
    <asset:javascript src="gokb/search.js" />
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">

        <div class="panel-body">
          <g:render template="qbeform" contextPath="." model="${[formdefn:qryconfig.qbeConfig?.qbeForm, 'hide':[], cfg:qryconfig.qbeConfig]}" />
        </div>

        <table id="yarmQResTable" class="table table-striped">
          <thead>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
      <pre id="dumpingGround">
      </pre>
    </div>
  </div>
</body>
</html>
