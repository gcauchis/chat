package com.gc.irc.server.client.connector.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.gc.irc.server.client.connector.AbstractClientConnector;
import com.gc.irc.server.client.connector.ClientConnector;

/**
 * 
 * @author gcauchis
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketClientConnector extends AbstractClientConnector implements ClientConnector, WebSocketConfigurer {

	@Override
	public void initConnector() {
		
	}

	@Override
	public void waitClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		
	}


}
