<form class="form-horizontal well" method="POST">
  <g:yarmTextField path="uriName" label="URI Name" placeholder="URI" value="${yrt.uriName}"/>
  <g:yarmTextField path="displayName" label="Display Name" placeholder="Display Name" value="${yrt.displayName}"/>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Save</button>
    </div>
  </div>
</form>
<form class="form-horizontal well">
  <g:yarmEmbeddedSearch label="Users" config="adm_users" context="${[memberOf:yrt.id]}"/>
</form>

