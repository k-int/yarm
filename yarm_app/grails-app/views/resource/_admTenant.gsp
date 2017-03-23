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
    <div class="panel-body">

      <g:yarmEmbeddedSearch label="Users" config="adm_users" context="${[qp_relparty:yrt.id]}"/>

      <g:form class="form-inline panel-body col-sm-10 col-sm-offset-2" controller="resource" action="processEmbeddedResource" method="post">

        <input type="hidden" name="cls" value="com.k_int.yarm.PartyRelationship"/>
        <input type="hidden" name="id" value="__new__"/>
        <input type="hidden" name="yrt.to" value="${yrt.id}"/>
        <input type="hidden" name="yrt.status" value=""/>

    
        <div class="form-group col-xs-2">
          <label class="col-sm-2 control-label">Add User</label>
        </div>

        <div class="form-group col-xs-7">
          <label for="yrt.from.id">User</label>
          <g:simpleReferenceTypedown id="userid_controller" name="yrt.from.id" mode="stdid" style="width:100%;" baseClass="com.k_int.yarm.auth.User" cssCls="form-control"/>
        </div>


        <div class="form-group col-xs-2">
          <label for="yrt.role.id">Role</label>
          <g:select name="yrt.role.id" 
                        class="form-control" 
                        from="${com.k_int.yarm.RefdataValue.executeQuery('select rdv from RefdataValue as rdv where rdv.owner.label=:l',[l:'relationshipRole'])}"
                        optionKey="id"
                        optionValue="${value}"
                        >
          </g:select>
        </div>

        <div class="form-group col-xs-1">
          <label>Action</label>
          <button class="form-control" type="submit" class="btn btn-default">Add</button>
        </div>

      </g:form>
    </div>
  </div>
</g:if>

