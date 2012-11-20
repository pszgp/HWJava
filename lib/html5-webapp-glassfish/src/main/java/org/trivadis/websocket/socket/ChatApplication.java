package org.trivadis.websocket.socket;

import java.io.IOException;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;

/**
 * This class represents a web socket application; it creates new sockets and
 * passes messages to all connected users
 * 
 * @author Andy Moncsek, Trivadis CH
 * 
 */
public class ChatApplication extends WebSocketApplication {

	@Override
	public WebSocket createSocket(WebSocketListener... listeners)
			throws IOException {
		return new ChatSocket(listeners);
	}

	@Override
	public void onMessage(WebSocket socket, DataFrame frame) throws IOException {
		final String data = frame.getTextPayload();
		for (final WebSocket webSocket : getWebSockets()) {
			try {
				// send data to all connected clients (including caller)
				webSocket.send(data);
			} catch (IOException e) {
				e.printStackTrace();
				webSocket.close();
			}
		}
	}

	@Override
	/**
	 * return true if you want to communicate over websocket otherwise return false
	 */
	public boolean isApplicationRequest(Request arg0) {
		return true;
	}

}
