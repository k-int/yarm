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
        GlobalDataSource form
        <form>
          <div class="form-group">
            <label for="__yr.identifier">Identifier</label>
            <input type="text" class="form-control" id="__yr.identifier" name="__yr.identifier" placeholder="Identifier" value="${__yr.identifier}">
          </div>
          <div class="form-group">
            <label for="__yr.name">Name</label>
            <input type="text" class="form-control" id="__yr.identifier" name="__yr.name" placeholder="Name" value="${__yr.name}">
          </div>
        </form>


  <pre>
 String identifier
  String name
  String type
  Date haveUpTo
  String uri
  String listPrefix
  String fullPrefix
  String principal
  String credentials
  Long rectype
  Boolean active
</pre>

${__yr}
      </div>
    </div>
  </div>
</body>
</html>
