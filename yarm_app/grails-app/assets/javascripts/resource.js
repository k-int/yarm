//= require jquery-2.2.0.min
//= require bootstrap
//= require select2/js/select2.min
//= require datatables.min

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

    $(".simpleReferenceTypedown").each(function(idx,value) {
      var dom = $(this).data('domain');
      var filter1 = $(this).data('filter1');
      var lookup_url = $(this).data('lookupurl');
      var mode = $(this).data('mode');

      $(value).select2({
        ajax: {
          url: lookup_url,
          dataType: 'json',
          delay: 250,
          data: function (params) {
            return {
              baseClass:dom,
              q: params.term, // search term
              mode: mode,
              page: params.page
            };
          },
          processResults: function (data, params) {
            // parse the results into the format expected by Select2
            // since we are using custom formatting functions we do not need to
            // alter the remote JSON data, except to indicate that infinite
            // scrolling can be used
            params.page = params.page || 1;

            return {
              results: data.values,
              pagination: {
                more: (params.page * 30) < data.total_count
              }
            };
          },
          cache: true
        },
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        // templateResult: formatRepo, // omitted for brevity, see the source of this page
        // templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
        minimumInputLength: 1,
      });


    });


  })(jQuery);


}
