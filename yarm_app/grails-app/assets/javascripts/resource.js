//= require application
//= require underscore
//= require jsv
//= require jsonform/jsonform-defaults
//= require jsonform/jsonform
//= require jsonform/jsonform-split


if (typeof jQuery !== 'undefined') {
  (function($) {
    console.log("resources.js");

      $('form').jsonForm({
        schema: {
          name: {
            type: 'string',
            title: 'Name',
            required: true
          },
          age: {
            type: 'number',
            title: 'Age'
          }
        },
        onSubmit: function (errors, values) {
          if (errors) {
            $('#res').html('<p>I beg your pardon?</p>');
          }
          else {
            $('#res').html('<p>Hello ' + values.name + '.' +
              (values.age ? '<br/>You are ' + values.age + '.' : '') +
              '</p>');
          }
        }
      });

  })(jQuery);


}
