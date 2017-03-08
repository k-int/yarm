<div class="form-group">
  <label for="yrt.identifier" class="col-sm-2 control-label">Identifier</label>
  <div class="col-sm-10">
    <input type="text" class="form-control" id="yrt.identifier" name="yrt.identifier" placeholder="Identifier" value="${yrt.identifier}">
  </div>
</div>

<div class="form-group">
  <label for="yrt.name" class="col-sm-2 control-label">Name</label>
  <div class="col-sm-10">
    <input type="text" class="form-control" id="yrt.identifier" name="yrt.name" placeholder="Name" value="${yrt.name}">
  </div>
</div>

<div class="form-group">
  <label for="yrt.name" class="col-sm-2 control-label">Name</label>
  <div class="col-sm-10">
    <input type="text" class="form-control" id="yrt.type" name="yrt.type" placeholder="Type" value="${yrt.type}">
  </div>
</div>

<g:yarmTextField path="haveUpTo" label="Latest Timestamp" value="${yrt.haveUpTo}" disabled="true"/>
<g:yarmTextField path="uri" label="URL" placeholder="URL" value="${yrt.uri}"/>
<g:yarmTextField path="listPrefix" label="List Prefix" placeholder="List Prefix" value="${yrt.listPrefix}"/>
<g:yarmTextField path="fullPrefix" label="Full Record Prefix" placeholder="Full Record Prefix" value="${yrt.fullPrefix}"/>
<g:yarmTextField path="principal" label="Username" placeholder="Username" value="${yrt.principal}"/>
<g:yarmTextField path="credentials" label="Password" placeholder="Password" value="${yrt.credentials}"/>

<!--
  Long rectype
  Boolean active
  ${yrt}
-->

