<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Topics</title>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
</head>
<body>
  <div class="container-fluid">
    <div class="row">
      <div class="container-fluid">
        <h1>Create an Organisation</h1>
        <h2>Set up the new organisation</h2>
        <p>
          Use this form to create a new organisation. Please carefully check the typedown to avoid
          creating duplicate organisations. If you find an existing record and feel you should be the
          owning authority for the record, please visit the directory page for that org and request
          administrative access.
        </p>

        <form>
          <div class="form-group">
            <label for="newOrganisationName">New Organisation Name</label>
            <select class="js-example-basic-single form-control" id="newOrganisationName" name="newOrganisationName" placeholder="New Organisation Name">
              <option value="AL">Alabama</option>
              <option value="WY">Wyoming</option>
            </select>
          </div>
          <button type="submit" class="btn btn-success">Create new organisation</button>
        </form>
        
      </div>
    </div>
  </div>
</body>
</html>



<content tag="footScripts">
  <script type="text/javascript">
    $(document).ready(function() {
      $(".js-example-basic-single").select2();
    });
  </script>
</content>
