package com.gc.irc.server.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.server.ServerStarter;

public class ServerTest {

	private static Thread starterThread;

	private static ServerStarter starter;

	private ConnectionThread connectionThread;

	@BeforeClass
	public static void init() {
		System.out.println("0");
		starter = new ServerStarter();
		starterThread = new Thread(starter);
		starterThread.start();
		System.out.println("1");
	}

	@Before
	public void prepare() throws InterruptedException {
		System.out.println("Wait for server to be up");
		while (!starter.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("2");
		connectionThread = new ConnectionThread(null, 1973);
		connectionThread.start();
		System.out.println("3");
		System.out.println("Wait for connectionThread to be up");
		while (!connectionThread.isInitialized()) {
			Thread.sleep(500);
		}
	}

	@AfterClass
	public static void cleanAll() {
		starterThread.interrupt();
	}

	@After
	public void clean() {
		connectionThread.interrupt();
	}

	@Test
	public void basicTest() throws InterruptedException {
		System.out.println("4");
		for (int i = 0; i < 5; i++) {
			System.out.println("send msg");
			sendMessage(connectionThread, getBasicMessage());
			Thread.sleep(500);
		}
	}

	private void sendMessage(final IIRCMessageSender messageSender,
			final IRCMessage message) {
		messageSender.send(message);
	}

	private IRCMessage getBasicMessage() {
		return new IRCMessageChat(0, null, null);
	}
}
