$(document).ready(function(){
   var simplemde = new SimpleMDE({
		element: document.getElementById("simple-mde"),
		toolbar: ["bold", "italic", "|", "quote", "strikethrough", "horizontal-rule", "heading-smaller",
		 "heading-bigger", "|", "code", "unordered-list", "ordered-list", "clean-block", "|",
		 "link", "image", "table", "|", "preview", "side-by-side", "fullscreen", "guide"]
	});
	
  $("#convert-to-html").click(function(){
	   var markdon_form = $("#markdown-form");
	   
	   if($(markdon_form).find("#simple-mde").length != 0)
	   	  $(markdon_form).find("#simple-mde").val(simplemde.value());
	   
       $.post(markdon_form.attr('action'), markdon_form.serialize(), function(data){
            $("#html-out").html(data.htmlResponse);
       })
       .fail(function(error) { 
	      console.log(error);
	      alert(error);
	    });
  });
  
});



document.querySelectorAll('.fom-submit-confirm').forEach(($item) => {
    $item.addEventListener('submit', (event) => {
        if (!confirm(event.currentTarget.getAttribute('data-msg-confirm'))) {
            event.preventDefault();
            return false;
        }
        return true;
    });
});
