//= require jquery-2.2.0.min
//= require bootstrap
//= require select2/js/select2.min
//= require datatables.min

if (typeof jQuery !== 'undefined') {
  (function($) {

    console.log("Resource....");
    // $('#yarmQResTable').DataTable(yarm_dt_config);

    // find all the elements with class dt_embedded_search and make them data table elements
    $('.dt_embedded_search').DataTable(yarm_dt_configs[$(this).data('config')]);

    // $('#__dbsearchForm').submit( function() {            
    //   console.log("Calling reload on data table...");
    //   $('#yarmQResTable').dataTable().reload();
    //   return false;
    // });

  })(jQuery);


}
