// search.js

if (typeof jQuery !== 'undefined') {
  (function($) {
    $('#__dbsearchForm').submit( function() {            

      console.log("submitform");

      $.ajax({
        url     : $(this).attr('action'),
        type    : $(this).attr('method'),
        dataType: 'json',
        data    : $(this).serialize(),
        success : function( data ) {
          console.log("o",data);
        },
        error   : function( xhr, err ) {
          console.log("o",err);
        }
      });    
      return false;
    });

  })(jQuery);


}
