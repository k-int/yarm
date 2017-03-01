// search.js

//= require datatables.min

if (typeof jQuery !== 'undefined') {
  (function($) {

    $('#yarmQResTable').DataTable();

    $('#__dbsearchForm').submit( function() {            

      console.log("submitform");

      $.ajax({
        url     : $(this).attr('action'),
        type    : $(this).attr('method'),
        dataType: 'json',
        data    : $(this).serialize(),
        success : function( data ) {
          console.log("o",data);
          $('#dumpingGround').html(data);
        },
        error   : function( xhr, err ) {
          console.log("o",err);
        }
      });    
      return false;
    });

  })(jQuery);


}
