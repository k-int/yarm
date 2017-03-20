<div class="form-group">
  <label for="yrt.${path}" class="col-sm-2 control-label">${label}</label>
  <form id="${config}_qryform">
    <input type="hidden" name="srch_cfg" value="${config}"/>
  </form>
  <div class="col-sm-10">
    <table class="dt_embedded_search" id="yarm_dt_${config}" data-dtconfig="${config}"></table>
  </div>
</div>

