<div class="panel panel-default">
  <div class="panel-heading">Agreement</div>

  <form class="form-horizontal panel-body" method="POST">
    <input type="hidden" name="yrt.owner" value="${(yrt.owner)?:(request.institution?.id)}"/>
    <p>${yrt.owner} ${request.institution?.id}</p>
    <g:yarmTextField path="name" label="Agreement Name" placeholder="Agreement Name" value="${yrt.name}"/>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">Save</button>
      </div>
    </div>
  </form>

</div>
