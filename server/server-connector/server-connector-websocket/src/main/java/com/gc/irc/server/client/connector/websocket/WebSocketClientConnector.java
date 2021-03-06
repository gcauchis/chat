package com.gc.irc.server.client.connector.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.gc.irc.server.client.connector.AbstractClientConnector;
import com.gc.irc.server.client.connector.ClientConnector;

/**
 * <p>WebSocketClientConnector class.</p>
 *
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
 * @author x472511
 * @version 0.0.4
 */
//@Configuration
//@EnableWebSocket
public class WebSocketClientConnector extends AbstractClientConnector implements ClientConnector, WebSocketConfigurer, WebSocketHandler {

	/** {@inheritDoc} */
	@Override
	public void initConnector() {
		// TODO Auto-generated method stub
	}

	/** {@inheritDoc} */
	@Override
	public void waitClient() {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(this, "/");
		
	}

	/** {@inheritDoc} */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}


}
