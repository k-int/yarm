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
        <form id="yarm_form_subject"
              data-content="${createLink(mapping:params.mapping, id:params.id, params:['format':'json'])}"
              data-schema="${createLink(controller:'config',action:'jsonSchema',id:params.jsonSchema)}"
              data-layout="${createLink(controller:'config',action:'layout',id:params.layout)}" ></form>

        <g:link mapping="${params.mapping}" id="${params.id}" params="[format:'json']">Data</g:link>
        <g:link controller="config" action="jsonSchema" id="${params.jsonSchema}">Schema</g:link>
        <g:link controller="config" action="layout" id="${params.layout}">Layout</g:link>

      </div>
    </div>
  </div>
  <asset:javascript src="resource.js" />
</body>
</html>
