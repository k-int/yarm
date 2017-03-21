<form class="form-horizontal" method="POST">
  
  <g:yarmTextField path="identifier" placeholder="Identifier" label="Identifier" value="${yrt.identifier}"/>
  <g:yarmTextField path="name" placeholder="Name" label="Name" value="${yrt.name}"/>
  <g:yarmTextField path="type" placeholder="Type" label="Type" value="${yrt.type}"/>
  <g:yarmTextField path="haveUpTo" label="Latest Timestamp" value="${yrt.haveUpTo}" disabled="true"/>
  <g:yarmTextField path="uri" label="URL" placeholder="URL" value="${yrt.uri}"/>
  <g:yarmTextField path="listPrefix" label="List Prefix" placeholder="List Prefix" value="${yrt.listPrefix}"/>
  <g:yarmTextField path="fullPrefix" label="Full Record Prefix" placeholder="Full Record Prefix" value="${yrt.fullPrefix}"/>
  <g:yarmTextField path="principal" label="Username" placeholder="Username" value="${yrt.principal}"/>
  <g:yarmTextField path="credentials" label="Password" placeholder="Password" value="${yrt.credentials}"/>
  
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Save</button>
    </div>
  </div>

</form>

<!--
  Long rectype
  Boolean active
  ${yrt}
-->

