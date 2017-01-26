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
        <div class="btn-group">
          <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            ${current_user.displayName} <span class="caret"></span>
          </button>
          <ul class="dropdown-menu">
            <li class="dropdown-header">Switch Dashboard Context</li>
            <g:each in="${user_contexts}" var="u">
              <li><g:link controller="directory" action="${u.type}Dash" id="${u.name}">${u.label}</g:link></li>
            </g:each>
            <li class="divider"></li>
            <li><g:link controller="account" action="organisations" id="new">New Organisation</g:link></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
