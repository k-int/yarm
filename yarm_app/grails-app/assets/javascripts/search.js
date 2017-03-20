// search.js

//= require jquery-2.2.0.min
//= require bootstrap
//= require select2/js/select2.min
//= require datatables.min

if (typeof jQuery !== 'undefined') {
  (function($) {

    console.log("cfg: %o",yarm_dt_config);

    $('#yarmQResTable').DataTable(yarm_dt_config);

    $('#__dbsearchForm').submit( function() {            
      console.log("Calling reload on data table...");
      $('#yarmQResTable').dataTable().reload();
      return false;
    });

  })(jQuery);


}
