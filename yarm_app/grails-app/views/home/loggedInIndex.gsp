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
            Action <span class="caret"></span>
          </button>
          <ul class="dropdown-menu">
            <g:each in="${user_contexts}" var="u">
              <li><g:link controller="directory" action="${u.type}" id="${u.name}">${u.label}</g:link></li>
            </g:each>
          </ul>
        </div>

      </div>
    </div>
  </div>
</body>
</html>
