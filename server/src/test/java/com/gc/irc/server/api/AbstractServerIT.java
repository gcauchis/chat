package com.gc.irc.server.api;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.ServerStarter;

/**
 * The Class AbstractServerIT.
 */
public abstract class AbstractServerIT {
	/** The Constant SERVER_PORT. */
	protected static final int SERVER_PORT = 1973;

	/** The starter thread. */
	private static Thread starterThread;

	/** The starter. */
	private static ServerStarter starter;
	
	/**
	 * Inits the.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@BeforeClass
	public static void init() throws InterruptedException {
		starter = new ServerStarter();
		starterThread = new Thread(starter);
		starterThread.start();
		System.out.println("Wait for server to be up");
		while (!starter.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("Server up");
	}
	
	/**
	 * Clean all.
	 */
	@AfterClass
	public static void cleanAll() {
		starterThread.interrupt();
	}
	
	/**
	 * Gets the connection to server.
	 *
	 * @return the connection to server
	 * @throws InterruptedException the interrupted exception
	 */
	protected ConnectionThread getConnectionToServer() throws InterruptedException {
		ConnectionThread connectionThread = new ConnectionThread(null, SERVER_PORT);
		connectionThread.start();
		System.out.println("Wait for connectionThread to be up");
		while (!connectionThread.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("ConnectionThread up");
		return connectionThread;
	}
	
	/**
	 * Send message.
	 * 
	 * @param messageSender
	 *            the message sender
	 * @param message
	 *            the message
	 */
	protected void sendMessage(final IIRCMessageSender messageSender,
			final IRCMessage message) {
		messageSender.send(message);
	}
}
