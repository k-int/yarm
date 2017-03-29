<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${request.institution?.displayName}</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>

  <ul class="breadcrumb">
    <li>${request.institution?.displayName} Titles</li>
    <li class="dropdown pull-right">
      <a class="dropdown-toggle btn btn-mini btn-primary" id="export-menu" role="button" data-toggle="dropdown" data-target="#" href="">Exports<b class="caret"></b></a>
      <ul class="dropdown-menu filtering-dropdown-menu" role="menu" aria-labelledby="export-menu">
        <li><g:link controller="institution" action="export" params="">JSON</g:link></li>
        <li><g:link controller="institution" action="export" params="">XML</g:link></li>
        <li><g:link controller="institution" action="export" params="">OCLC Resolver</g:link></li>
        <li><g:link controller="institution" action="export" params="">Serials Solutions Resolver</g:link></li>
        <li><g:link controller="institution" action="export" params="">SFX Resolver</g:link></li>
      </ul>
    </li>
  </ul>


  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">
        <div class="form-group">
  	  <label class="col-sm-2 control-label" for="qp_name">Title Search:</label>
  	  <div class="col-sm-10">
            <div class="">
              <input class="form-control" type="text" name="qp_name" id="qp_name" placeholder="Name or title of item" value="" />
            </div>
  	  </div>
        </div>
      </div>
    </div>
    <div class="row">
      <table class="table">
        <thead>
          <tr>
            <th colspan="9" style="text-align:center;">Found ${titleCount} titles available to this institution</th>
          </tr>
          <tr>
            <th rowspan="2">Title</th>
            <th rowspan="2">Platform</th>
            <th colspan="3">Start</th>
            <th colspan="3">End</th>
          </tr>
          <tr>
            <th>Volume</th>
            <th>Issue</th>
            <th>Date</th>
            <th>Volume</th>
            <th>Issue</th>
            <th>Date</th>
          </td>
        </thead>
        <tbody>
          <g:each in="${titles}" var="grpp">
            <tr>
              <td>${grpp.global_resource.name}</td>
              <td>${grpp.plat.name}</td>
              <td>${grpp.startVolume}</td>
              <td>${grpp.startIssue}</td>
              <td><g:formatDate date="${grpp.startDate}" format="yyyy-MM-dd" timeZone="UCT"/></td>
              <td>${grpp.endVolume}</td>
              <td>${grpp.endIssue}</td>
              <td><g:formatDate date="${grpp.endDate}" format="yyyy-MM-dd" timeZone="UCT"/></td>
            </tr>
            <tr> 
              <td colspan="9" class="no-top-border" >coverage note:${grpp.coverageNote?:'none'}; coverage depth:${grpp.coverageDepth?.value?:'none'}; embargo:${grpp.embargo?:'none'}</td>
            </tr>
          </g:each>
        </tbody>
      </table>
    </div>

  </div>
  <asset:javascript src="application.js" />
</body>
</html>

