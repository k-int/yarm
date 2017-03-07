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
        <p>This is the edit page</p>
        <form data-content="${createLink(mapping:params.mapping, id:params.id, params:['format':'json'])}"
              data-schema="${createLink(controller:'resource',action:'schema',params:params)}"
              data-layout="${createLink(controller:'resource',action:'layout',params:params)}" ></form>

        <g:link mapping="${params.mapping}" id="${params.id}" params="[format:'json']">Link</g:link>

      </div>
    </div>
  </div>
  <asset:javascript src="resource.js" />
</body>
</html>
