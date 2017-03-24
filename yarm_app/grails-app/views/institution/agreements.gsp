<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${tenant.displayName} - Agreements</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">
        <h1>${tenant.displayName} : Agreements</h1>
        <table class="table table-striped">
          <thead>
            <th>Agreement Name</th>
            <th>Owner</th>
            <th>Status</th>
          </thead>
          <g:each in="${agreements}" var="a">
            <tr>
              <td>${a.name}</td>
              <td>${a.owner.displayName}</td>
              <td></td>
            </tr>
          </g:each>
        </table>
      </div>
    </div>
  </div>
  <asset:javascript src="application.js" />
</body>
</html>

