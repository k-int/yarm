// search.js

//= require application
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
