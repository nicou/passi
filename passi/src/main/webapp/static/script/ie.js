$(document).ready(function() {
  var $style;
  $style = $('<style type="text/css">:before,:after{content:none !important}</style>');
  $('head').append($style);
  return setTimeout((function() {
    return $style.remove();
  }), 0);
});