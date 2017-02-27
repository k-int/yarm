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

        <div class="panel-body">
          <g:render template="qbeform" contextPath="." model="${[formdefn:qryconfig.qbeConfig?.qbeForm, 'hide':[], cfg:qryconfig.qbeConfig]}" />
        </div>

        <table class="table table-striped">
          <thead>
            <tr>
              <g:each in="${qryconfig.qbeConfig.qbeResults}" var="qbeCol">
                <th>
                  ${message(code: qbeCol.labelKey)} 
                </th>
              </g:each>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table>

        <pre>
          ${qryconfig}
        </pre>
      </div>
    </div>
  </div>
</body>
</html>
