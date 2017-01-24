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
      $(".js-example-basic-single").select2( {
        ajax: {
          url: "<g:createLink controller="ajaxSupport" action="lookup" params="[baseClass:'com.k_int.yarm.Org']"/>",
          dataType: 'json',
          delay: 250,
          data: function (params) {
            return {
              q: params.term, // search term
              page: params.page
            };
          },
          processResults: function (data, params) {
            params.page = params.page || 1;
            return {
              results: data.values,
              pagination: {
                more: (params.page * 30) < data.total_count
              }
            };
          },
          cache: true
        },
        minimumInputLength: 1,
        tags:true,
        createTag: function (params) {
          console.log("Create new org :: %o",params);
          return {
            id: "__new__",
            text: params.term,
            newOption: true
          }
        }
      });
    });
  </script>
</content>
