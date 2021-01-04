$(document).ready(function () {
    $(".cartItemQty").on("change", function () {
      var id = this.id;
      var qty = this.value;
      $("#newqty").val(qty);
      $("#newid").val(id);
      $("#update-item-" + id).css("display", "inline-block");
    });
});