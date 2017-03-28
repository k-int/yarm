
console.log("agreement.js");

if (typeof jQuery !== 'undefined') {
  console.log("one");
  (function($) {

    console.log("two");

    $("#findPkgBtn").click(
      function(event)  {
        var lookupUrl = $(this).data('lookup');
        var pkg_search_text = $('#pkgSearchTxt').val();
        var instroot = $('#appBaseDiv').data('instroot');
        console.log("cleek url:%s",lookupUrl);
        event.preventDefault();
        $.ajax({
          url: lookupUrl,
          data:{
              baseClass:'com.k_int.yarm.Package',
              q: pkg_search_text
          }
        }).done(function(data) {
          console.log("%o",data);
          $('#pkgListTableBody').empty();
          $.each(data.values, function(i, item) {
            console.log("Adding %o",item);
            $('#pkgListTableBody').append("<tr><td>"+item.text+"</td><td></td><td></td><td>"+
                                            "<a href=\""+instroot+"/addToAgreement?pkg="+item.id+"\" class=\"btn btn-success\">Add</a>"+
                                            "</td></tr>");
          });
          console.log("done");
        });
      }
    );


  })(jQuery);
}

function wibble() {
  console.log("wibble");
}
