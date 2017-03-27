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
              <td><g:link mapping="tenantAgreement" id="${a[0].id}" params="${[institution_shortcode:params.institution_shortcode]}">${a[0].name}</g:link></td>
              <td>${a[0].owner.displayName}</td>
              <td>
                <g:if test="${a[1]}">
                  ${a[1].status.value}
                </g:if>
                <g:else>
                  Available
                </g:else>
              </td>
            </tr>
          </g:each>
        </table>
      </div>
    </div>
  </div>
  <asset:javascript src="application.js" />
</body>
</html>

