package org.trivadis.websocket.socket;

import com.sun.grizzly.websockets.BaseServerWebSocket;
import com.sun.grizzly.websockets.WebSocketListener;
/**
 * This class represents a chat socket associated with a client
 * 
 * @author Andy Moncsek, Trivadis CH
 * 
 */
public class ChatSocket extends BaseServerWebSocket {
	public ChatSocket(WebSocketListener... listeners) {
		super(listeners);
	}
}
