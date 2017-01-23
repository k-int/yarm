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
        Switch Context:
        <select class="select2tag">
          <option value="AL">Alabama</option>
          <option value="WY">Wyoming</option>
        </select>
      </div>
    </div>
  </div>
</body>
</html>

<content tag="footScripts">
  <script language="javascript">
    $(document).ready(function() {
      alert("OK");
      $(".select2tag").select2();
    });
  </script>
</content>
                      
