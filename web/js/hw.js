//source: http://java.dzone.com/articles/creating-websocket-chat

var ws;
$(document).ready(
	function() {
		ws = new WebSocket("http://localhost:8080/HWJavaSocket");
		ws.onopen = function(event) {					
		}
		ws.onmessage = function(event) {
			var $textarea = $('#messages');
			$textarea.val($textarea.val() + event.data + "\n");
		}
		ws.onclose = function(event) {					
		}
	});
        
        
function sendMessage() {
    var message = $('#username').val() + ":" + $('#message').val();
    ws.send(message);
    $('#message').val('');
}
									

								
