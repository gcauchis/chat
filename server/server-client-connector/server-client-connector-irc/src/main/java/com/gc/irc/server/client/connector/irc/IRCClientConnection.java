package com.gc.irc.server.client.connector.irc;

import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.AbstractClientSocketConnection;

public class IRCClientConnection extends
		AbstractClientSocketConnection<BufferedInputStream, BufferedOutputStream> {

	public IRCClientConnection(Socket clientSocket) {
		super(clientSocket);
	}

	@Override
	protected BufferedInputStream build(InputStream inputStream)
			throws IOException {
		return new BufferedInputStream(inputStream);
	}

	@Override
	protected BufferedOutputStream build(OutputStream outputStream)
			throws IOException {
		return new BufferedOutputStream(outputStream);
	}

	@Override
	protected Message receiveMessage(BufferedInputStream inputStream)
			throws ClassNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(inputStream, Charset.defaultCharset());
		getLog().info("lines: "+ lines);
		return null;
	}

	@Override
	protected void send(BufferedOutputStream outputStream, Message message)
			throws IOException {
		getLog().info("send not yet implementted");
		
	}

}
