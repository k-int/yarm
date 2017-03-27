
console.log("agreement.js");

if (typeof jQuery !== 'undefined') {
  console.log("one");
  (function($) {

    console.log("two");

    $("#findPkgBtn").click(
      function(event)  {
        var lookupUrl = $(this).data('lookup');
        console.log("cleek url:%s",lookupUrl);
        event.preventDefault();
        $.ajax({
          url: lookupUrl,
          data:{
              baseClass:'com.k_int.yarm.Package',
              q: ''
          }
        }).done(function(data) {
          console.log("%o",data);
        });
      }
    );


  })(jQuery);
}

function wibble() {
  console.log("wibble");
}
