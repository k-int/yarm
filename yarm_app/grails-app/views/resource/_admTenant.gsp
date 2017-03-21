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

<g:if test="${yrt.id}">
  <div class="panel panel-default">
    <div class="panel-heading">${yrt.displayName} - Additional</div>
    <g:form class="form-horizontal panel-body" controller="resource" action="processChild" method="post">
  
      <g:yarmEmbeddedSearch label="Users" config="adm_users" context="${[qp_relparty:yrt.id]}"/>

      <input type="hidden" name="yrt.to" value="${yrt.id}"/>

      <div class="form-group">
        <label for="yrt.from" class="col-sm-2 control-label">Add User</label>
        <div class="col-sm-10">
          <div class="input-group">
            <input class="form-control" style="width:50%;" type="text" name="username" />
            <select class="form-control" style="width:50%;" name="role">
              <option value="1">Member</option>
            </select>
            <span class="input-group-btn">
              <button class="form-control" type="submit" class="btn btn-default">Add</button>
            </span>
          </div>
        </div>
      </div>

    </g:form>
  </div>
</g:if>

