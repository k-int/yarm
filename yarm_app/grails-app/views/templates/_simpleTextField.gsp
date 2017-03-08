<div class="form-group">
  <label for="yrt.${path}" class="col-sm-2 control-label">${label}</label>
  <div class="col-sm-10">
    <input type="text" 
           class="form-control" 
           id="yrt.${path}" 
           name="yrt.${path}" 
           placeholder="${placeholder}" 
           value="${value}"
           ${disabled=='true'?'disabled':''}>
  </div>
</div>

