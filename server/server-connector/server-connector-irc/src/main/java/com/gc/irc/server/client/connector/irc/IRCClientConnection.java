package com.gc.irc.server.client.connector.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.AbstractClientSocketConnection;

/**
 * <p>IRCClientConnection class.</p>
 *
 * @version 0.0.5
 * @author x472511
 */
public class IRCClientConnection extends
		AbstractClientSocketConnection<InputStream, OutputStream> {
	
	private BufferedReader bufferedReader;

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
	protected InputStream build(InputStream inputStream)
			throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		return inputStream;
	}

	/** {@inheritDoc} */
	@Override
	protected OutputStream build(OutputStream outputStream)
			throws IOException {
		return outputStream;
	}

	/** {@inheritDoc} */
	@Override
	protected Message receiveMessage(InputStream inputStream)
			throws ClassNotFoundException, IOException {
		getLog().debug(getId()+ " waiting message");
		String line = bufferedReader.readLine();
		Message message = parseLine(line);
		return message;
	}

	private Message parseLine(String line) {
		getLog().info("lines: "+ line);
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected void send(OutputStream outputStream, Message message)
			throws IOException {
		getLog().info("send not yet implementted");
		
	}

}
