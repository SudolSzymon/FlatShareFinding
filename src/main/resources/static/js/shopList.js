$(document).ready(function () {
  let  listName = $("#shopListName").text();
  $(".btnDelete").click( function () {
    let row = $(this).closest('tr');
    $.post('/shop/delete/' + listName + '/' + row.attr('id'));
    row.remove();
  });
});
