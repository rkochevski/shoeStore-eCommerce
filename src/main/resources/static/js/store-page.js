var sidebarContent = $('.accordion-sidebar').html()

$('#filterModal').on('show.bs.modal', function () {
  // move filter contents to modal body
  $(this).find('.modal-body').html('<div class="accordion accordion-caret accordion-sidebar accordion-modal">'+sidebarContent+'</div>')
  // empty the sidebar filter contents
  $('.accordion-sidebar:not(.accordion-modal)').html('')
}) 
$('#filterModal').on('hidden.bs.modal', function () {
	$('.accordion-modal').remove() // remove modal filter contents
  	$('.accordion-sidebar').html(sidebarContent) // move filter contents back to the sidebar
})  
$('.button-sort').click(function () {
  var value = $(this).val()
  var text = $(this).text()      
  $('#input-sort').val(value)
  $('#dropdown-sort-text').text(text)      
  $('#filter-form').submit()     
}) 

$(function() {    
	$('#light-pagination').pagination({
      items: $('#totalitems').val(),
      itemsOnPage: $('#itemsperpage').val(),
      currentPage: $('#page').val(),
      cssStyle: 'light-theme',
      useAnchors: false,
      prevText: "<",
      nextText: ">",
      displayedPages: 1,
      edges: 1,
      onPageClick: function(pageNumber) {
    	$('#page').val(pageNumber)
        $('#filter-form').submit()     
      }
	});
});