// search.js

//= require datatables.min

if (typeof jQuery !== 'undefined') {
  (function($) {

    console.log("cfg: %o",yarm_dt_config);

    $('#yarmQResTable').DataTable(yarm_dt_config);

    $('#__dbsearchForm').submit( function() {            


      // var search_data = $('#__dbsearchForm').serialize();
      // console.log("submitform %o",search_data);

      console.log("Calling reload on data table...");
      $('#yarmQResTable').dataTable().reload();

      // $.ajax({
      //   url     : $(this).attr('action'),
      //   type    : $(this).attr('method'),
      //   dataType: 'json',
      //   data    : $(this).serialize(),
      //   success : function( data ) {
      //     console.log("o",data);
      //     $('#dumpingGround').html(data);
      //   },
      //   error   : function( xhr, err ) {
      //     console.log("o",err);
      //   }
      // });    
      // return false;
    });

  })(jQuery);


}
