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
            <div class="input-group">
              <input type="text" class="form-control" id="newOrganisationName" name="newOrganisationName" placeholder="New Organisation Name">
              <span class="input-group-btn">
                <button id="newOrgBtn" type="submit" class="btn btn-error">Create new organisation</button>
              </span>
            </div>
          </div>
        </form>
        
      </div>
    </div>
  </div>
</body>
</html>



<content tag="footScripts">
  <script type="text/javascript">
    $(document).ready(function() {
       $( "#newOrgBtn" ).prop('disabled', true);
       var e = document.getElementById('newOrganisationName');
       e.oninput = validateNewOrgName;
       e.onpropertychange = e.oninput;
    });

    function validateNewOrgName(event) {
      console.log ("The new content %o", event.target.value);
      $.ajax({
        dataType: "json",
        url: "<g:createLink controller='account' action='validateProposedOrg'/>?name="+event.target.value
      }).done(function( data ) {
        if ( data.isOk ) {
            $( "#newOrgBtn" ).prop('disabled', false);
            $( "#newOrgBtn" ).removeClass( "btn-error" );
            $( "#newOrgBtn" ).addClass( "btn-success" );
        }
        else {
            $( "#newOrgBtn" ).prop('disabled', true);
            $( "#newOrgBtn" ).removeClass( "btn-success" );
            $( "#newOrgBtn" ).addClass( "btn-error" );
        }

        console.log("Data:%o",data);
      });
    }
  </script>
</content>
