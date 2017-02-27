// search.js

if (typeof jQuery !== 'undefined') {
  (function($) {

    $('#__dbsearchForm').submit( function() {            
      $.ajax({
        url     : $(this).attr('action'),
        type    : $(this).attr('method'),
        dataType: 'json',
        data    : $(this).serialize(),
        success : function( data ) {
          alert('Submitted');
        },
        error   : function( xhr, err ) {
          alert('Error');     
        }
      });    
      return false;
    });
  });

}
