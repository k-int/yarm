<div class="panel panel-default">
  <div class="panel-heading">${yrt.displayName} - Core</div>

  <form class="form-horizontal panel-body" method="POST">
    <g:yarmTextField path="uriName" label="URI Name" placeholder="URI" value="${yrt.uriName}"/>
    <g:yarmTextField path="displayName" label="Display Name" placeholder="Display Name" value="${yrt.displayName}"/>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">Save</button>
      </div>
    </div>
  </form>

</div>

<div class="panel panel-default">
  <div class="panel-heading">${yrt.displayName} - Additional</div>
  <form class="form-horizontal panel-body">
    <g:yarmEmbeddedSearch label="Users" config="adm_users" context="${[qp_relparty:yrt.id]}"/>
  </form>
</div>

