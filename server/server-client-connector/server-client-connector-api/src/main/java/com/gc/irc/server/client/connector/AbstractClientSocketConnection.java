package com.gc.irc.server.client.connector;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.core.user.management.UserManagementAware;

/**
 * <p>Abstract AbstractClientSocketConnection class.</p>
 *
 * @author gcauchis
 * @version 0.0.5
 */
public abstract class AbstractClientSocketConnection<IN extends InputStream, OUT extends OutputStream> extends
		AbstractClientConnection implements ClientConnection,
		UserManagementAware {

	/** The client socket. */
	private Socket clientSocket;

	/** The in object. */
	private IN inputStream;

	/** The out object. */
	private OUT outputStream;

	/**
	 * Builder who initialize the TCP connection.
	 *
	 * @param clientSocket
	 *            Client's Socket.
	 */
	public AbstractClientSocketConnection(final Socket clientSocket) {
		init(clientSocket);
	}

	/**
	 * <p>
	 * init.
	 * </p>
	 *
	 * @param clientSocket
	 *            a {@link java.net.Socket} object.
	 */
	private void init(final Socket clientSocket) {
		getLog().info(getId() + " Initialisation du thread.");
		this.clientSocket = clientSocket;

		try {
			getLog().debug(getId() + " Create inObject");
			inputStream = build(clientSocket.getInputStream());
			getLog().debug(getId() + " Create outObject");
			outputStream = build(clientSocket.getOutputStream());

		} catch (final IOException e) {
			getLog().warn(
					getId() + " Fail to open Client's Steams to "
							+ clientSocket.getInetAddress() + " : ", e);
		}
		getLog().debug(getId() + " end init");
	}

	/** {@inheritDoc} */
	@Override
	protected void disconnect() {
		/**
		 * Closing Socket.
		 */
		try {
			getLog().info(
					getId() + " Closing Client's connection "
							+ clientSocket.getInetAddress() + ".");
			if (!clientSocket.isInputShutdown()) {
				getLog().debug(getId() + " Closing inObject");
				inputStream.close();
			}
			if (!clientSocket.isOutputShutdown()) {
				getLog().debug(getId() + " Closing outObject");
				outputStream.close();
			}
			if (!clientSocket.isClosed()) {
				getLog().debug(getId() + " Closing clientSocket");
				clientSocket.close();
			}
		} catch (final IOException e) {
			getLog().warn(
					getId() + " Fail to close Client's connection "
							+ clientSocket.getInetAddress() + " : "
							+ e.getMessage());
		}

	}

	/**
	 * Wait and Receive a message send by the client.
	 *
	 * @return Message received.
	 */
	public Message receiveMessage() {
		Message message = null;
		try {
			getLog().debug(getId() + " Wait for a message in the socket.");
			message = receiveMessage(inputStream);
			checkMessage(message);
		} catch (final EOFException e) {
			getLog().debug(getId() + " Stream seem to be closed by client.");
			socketAlive();
		} catch (final ClassNotFoundException | IOException e) {
			getLog().info(getId() + " Fail to receive a message : ", e);
			socketAlive();
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gc.irc.server.thread.impl.IGestionClientBean#envoyerMessageObjetSocket
	 * (com.gc.irc.common.protocol.IRCMessage)
	 */
	/** {@inheritDoc} */
	@Override
	public void send(final Message message) {
		try {
			/**
			 * Synchronize the socket.
			 */
			if (!clientSocket.isOutputShutdown()) {
				synchronized (inputStream) {
					synchronized (outputStream) {
						getLog().debug(
								getId()
										+ " Send message to "
										+ (getUser() == null ? "Unkwown"
												: getUser().getNickName()));
						if (clientSocket.isConnected()) {
							send(outputStream, message);
						} else {
							getLog().warn(getId() + " Socket not connected !");
						}
					}
				}
			} else {
				getLog().warn(
						getId()
								+ " Fail to send message. Finalize because output is shutdown.");
				disconnectUser();
			}
		} catch (final IOException e) {
			getLog().warn(
					getId() + " Fail to send the message : " + e.getMessage());
			socketAlive();
		}
	}

	/**
	 * Test if the socket is already open. If socket is closed or a problem is
	 * remark the thread is finalize.
	 */
	private void socketAlive() {
		getLog().debug(getId() + " Test if the socket have no problem.");
		if (clientSocket.isClosed() || clientSocket.isInputShutdown()
				|| clientSocket.isOutputShutdown() || !clientSocket.isBound()
				|| !clientSocket.isConnected()) {
			getLog().error(
					getId()
							+ " A problem find on the Socket. Closing Connection");
			disconnectUser();
		}
	}
	
	/**
	 * <p>build.</p>
	 *
	 * @param inputStream a {@link java.io.InputStream} object.
	 * @return a IN object.
	 * @throws java.io.IOException if any.
	 */
	protected abstract IN build(InputStream inputStream) throws IOException;
	
	/**
	 * <p>build.</p>
	 *
	 * @param outputStream a {@link java.io.OutputStream} object.
	 * @return a OUT object.
	 * @throws java.io.IOException if any.
	 */
	protected abstract OUT build(OutputStream outputStream) throws IOException;
	
	/**
	 * <p>receiveMessage.</p>
	 *
	 * @param inputStream a IN object.
	 * @return a {@link com.gc.irc.common.protocol.Message} object.
	 * @throws java.lang.ClassNotFoundException if any.
	 * @throws java.io.IOException if any.
	 */
	protected abstract Message receiveMessage(IN inputStream) throws ClassNotFoundException, IOException;
	
	/**
	 * <p>send.</p>
	 *
	 * @param outputStream a OUT object.
	 * @param message a {@link com.gc.irc.common.protocol.Message} object.
	 * @throws java.io.IOException if any.
	 */
	protected abstract void send(OUT outputStream, Message message) throws IOException;

}
