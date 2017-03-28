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
            <th>Selection Status</th>
            <th>Action</th>
          </thead>
          <g:each in="${agreements}" var="a">
            <tr>
              <td><g:link mapping="tenantAgreement" id="${a[0].id}" params="${[institution_shortcode:params.institution_shortcode]}">${a[0].name}</g:link></td>
              <td>${a[0].owner.displayName}</td>
              <td>
                <g:if test="${a[1]}">
                  ${a[1].status.value} (${a[1].activeYN?.value?:'Disabled'})
                </g:if>
                <g:else>
                  Available
                </g:else>
              </td>
              <td>
                <g:if test="${a[1]==null}">
                  <g:link mapping="selectAgreement" params="${[institution_shortcode:params.institution_shortcode,agreement:a[0].id]}">Select</g:link>
                </g:if>
                <g:else>
                  <g:if test="${a[1].activeYN?.value=='Yes'}">
                    <g:link mapping="updateAgreementStatus" params="${[institution_shortcode:params.institution_shortcode,asig:a[1].id,status:'No']}">Disable</g:link>
                  </g:if>
                  <g:else>
                    <g:link mapping="updateAgreementStatus" params="${[institution_shortcode:params.institution_shortcode,asig:a[1].id,status:'Yes']}">Enable</g:link>
                  </g:else>
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

