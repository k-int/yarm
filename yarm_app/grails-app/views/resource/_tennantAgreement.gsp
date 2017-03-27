<div class="panel panel-default">
  <div class="panel-heading">Agreement</div>

  <form class="form-horizontal panel-body" method="POST">
    <input type="hidden" name="yrt.owner" value="${(yrt.owner)?:(request.institution?.id)}"/>
    <g:yarmTextField path="name" label="Agreement Name" placeholder="Agreement Name" value="${yrt.name}"/>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit" class="btn btn-default">Save</button>
      </div>
    </div>
  </form>

</div>

<div class="panel panel-default">
  <div class="panel-heading">Contents</div>
  <button type="button" name="" class="btn btn-success" data-toggle="modal" data-target="#addTitleModal">Add Title To Agreement</button>
</div>



<div id="addTitleModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
        <p>Some text in the modal.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<div id="addPackageModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
        <p>Some text in the modal.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>


