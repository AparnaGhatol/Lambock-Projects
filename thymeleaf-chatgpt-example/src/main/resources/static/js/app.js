$(document).ready(function(){
	
  $("#chat-btn").click(function(){
	$("#chat-response").append(spinner());
	var chatMsg = $("#input-message").val();
    $.ajax({url: "/openai/chat/"+chatMsg, success: function(result){
	   $(".spinner-block").remove();
       $("#chat-response").append(chatResponse(result.prompt, result.htmlResponse));
       hljs.highlightAll();
       $("#input-message").val('');
    }});
  });
  
  
  function chatResponse(prompt, response) {
	
	  return `<div class="card  mb-4 shadow-sm bg-light">
				  <div class="card-header">
				    <h6>${prompt}</h6>
				  </div>
				  <div class="card-body">
				    <p class="card-text">${response}</p>			
				  </div>
				  <div class="card-footer"><button class="btn btn-secodary float-md-end" onClick="window.location.reload();">Reset Chat</button></div>
				</div>`;
	  }
});

function spinner(){
	return `<div class="d-flex justify-content-center spinner-block">
			  <div class="spinner-border" role="status">
			    <span class="visually-hidden">Loading...</span>
			  </div>
			</div>`;
}

document.querySelectorAll('.fom-submit-confirm').forEach(($item) => {
    $item.addEventListener('submit', (event) => {
        if (!confirm(event.currentTarget.getAttribute('data-msg-confirm'))) {
            event.preventDefault();
            return false;
        }
        return true;
    });
});
