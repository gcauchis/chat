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

/**
 * <p>IRCClientConnection class.</p>
 *
 * @author gcauchis
 * @version 0.0.5
 */
public class IRCClientConnection extends
		AbstractClientSocketConnection<BufferedInputStream, BufferedOutputStream> {

	/**
	 * <p>Constructor for IRCClientConnection.</p>
	 *
	 * @param clientSocket a {@link java.net.Socket} object.
	 */
	public IRCClientConnection(Socket clientSocket) {
		super(clientSocket);
	}

	/** {@inheritDoc} */
	@Override
	protected BufferedInputStream build(InputStream inputStream)
			throws IOException {
		return new BufferedInputStream(inputStream);
	}

	/** {@inheritDoc} */
	@Override
	protected BufferedOutputStream build(OutputStream outputStream)
			throws IOException {
		return new BufferedOutputStream(outputStream);
	}

	/** {@inheritDoc} */
	@Override
	protected Message receiveMessage(BufferedInputStream inputStream)
			throws ClassNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(inputStream, Charset.defaultCharset());
		getLog().info("lines: "+ lines);
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected void send(BufferedOutputStream outputStream, Message message)
			throws IOException {
		getLog().info("send not yet implementted");
		
	}

}
