//= require jquery-2.2.0.min
//= require bootstrap
//= require select2/js/select2.min
//= require datatables.min
//= require typeahead.bundle

var datasrc = function(query, syncResults, asyncResults) {
  console.log("%o,%o,%o",query, syncResults, asyncResults);
};

if (typeof jQuery !== 'undefined') {
  (function($) {

    console.log("Resource....");
    // $('#yarmQResTable').DataTable(yarm_dt_config);

    // find all the elements with class dt_embedded_search and make them data table elements
    // $('.dt_embedded_search').DataTable(yarm_dt_configs[$(this).data('dtconfig')]);
    $('.dt_embedded_search').each(function(index) {
      $(this).DataTable(yarm_dt_configs[$(this).data('dtconfig')]);
    });

    // $('#__dbsearchForm').submit( function() {            
    //   console.log("Calling reload on data table...");
    //   $('#yarmQResTable').dataTable().reload();
    //   return false;
    // });

    $(".simpleReferenceTypedown").each(function(elem) {
      var dom = $(this).data('domain');
      var filter1 = $(this).data('filter1');


      $(this).typeahead(
        {}, 
        {
          name: 'best-pictures',
          source: datasrc
        });

    });


  })(jQuery);


}
