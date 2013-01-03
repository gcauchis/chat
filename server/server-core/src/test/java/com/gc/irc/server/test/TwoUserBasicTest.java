package com.gc.irc.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.message.api.IClientMessageLine;
import com.gc.irc.common.message.impl.BasicClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.api.AbstractServerTest;
import com.gc.irc.server.test.handler.SimpleMessageHandler;

/**
 * The Class TwoUserBasicTestIT.
 */
public class TwoUserBasicTest extends AbstractServerTest {

	/** The connection user1. */
	private ConnectionHandler connectionUser1;

	/** The connection user2. */
	private ConnectionHandler connectionUser2;

	/** The user1. */
	private IRCUser user1;

	/** The user2. */
	private IRCUser user2;

	/**
	 * Builds the simple message.
	 * 
	 * @param user
	 *            the user
	 * @param message
	 *            the message
	 * @return the iRC message
	 */
	private IRCMessage buildSimpleMessage(final IRCUser user,
			final String message) {
		return new IRCMessageChat(user.getId(),
				Arrays.asList((IClientMessageLine) new BasicClientMessageLine(
						message)));
	}

	/**
	 * Builds the simple private message.
	 * 
	 * @param user
	 *            the user
	 * @param message
	 *            the message
	 * @param toId
	 *            the to id
	 * @return the iRC message
	 */
	private IRCMessage buildSimplePrivateMessage(final IRCUser user,
			final String message, int toId) {
		return new IRCMessageChatPrivate(user.getId(),
				Arrays.asList((IClientMessageLine) new BasicClientMessageLine(
						message)), toId);
	}

	/**
	 * Check simple message receive.
	 * 
	 * @param userSources
	 *            the user sources
	 * @param messageStr
	 *            the message str
	 * @param receivedChatMessage
	 *            the received chat message
	 */
	private void checkSimpleMessageReceive(final IRCUser userSources,
			final String messageStr, final IRCMessageChat receivedChatMessage) {
		assertEquals(userSources.getId(), receivedChatMessage.getFromId());
		assertNotNull(receivedChatMessage.getLines());
		assertEquals(1, receivedChatMessage.getLines().size());
		final IClientMessageLine clientMessageLine = receivedChatMessage
				.getLines().get(0);
		assertNotNull(clientMessageLine);
		assertTrue(clientMessageLine instanceof BasicClientMessageLine);
		final BasicClientMessageLine basicClientMessageLine = (BasicClientMessageLine) clientMessageLine;
		assertEquals(messageStr, basicClientMessageLine.getMessage());
	}

	/**
	 * Clean.
	 */
	@After
	public void clean() {
		connectionUser1.disconnect();
		connectionUser2.disconnect();
	}

	/**
	 * Basic test.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Test
	public void globalConvers() throws InterruptedException {
		final SimpleMessageHandler messageHandlerUser1 = new SimpleMessageHandler();
		connectionUser1.setMessageHandler(messageHandlerUser1);
		final SimpleMessageHandler messageHandlerUser2 = new SimpleMessageHandler();
		connectionUser2.setMessageHandler(messageHandlerUser2);
		user1SendToGlobalWith2Listening("FisrtMessage", messageHandlerUser2);
		user2SendToGlobalWith1Listening("SecondMessage", messageHandlerUser1);
		user1SendToGlobalWith2Listening("ThirdMessage", messageHandlerUser2);
		user2SendToGlobalWith1Listening("FourthMessage", messageHandlerUser1);
		user2SendToGlobalWith1Listening("FiftMessage", messageHandlerUser1);
	}

	/**
	 * Prepare.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Before
	public void prepare() throws InterruptedException {
		connectionUser1 = getConnectionToServer();
		user1 = loginAndRegister(connectionUser1,
				"TwoUserBasicTestITUser1Login",
				"TwoUserBasicTestITUser1Password");
		assertNotNull(user1);
		connectionUser2 = getConnectionToServer();
		user2 = loginAndRegister(connectionUser2,
				"TwoUserBasicTestITUser2Login",
				"TwoUserBasicTestITUser2Password");
		assertNotNull(user2);
	}

	/**
	 * Private convers.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Test
	public void privateConvers() throws InterruptedException {
		final SimpleMessageHandler messageHandlerUser1 = new SimpleMessageHandler();
		connectionUser1.setMessageHandler(messageHandlerUser1);
		final SimpleMessageHandler messageHandlerUser2 = new SimpleMessageHandler();
		connectionUser2.setMessageHandler(messageHandlerUser2);
		user1ToUser2("FisrtMessage", messageHandlerUser2);
		user2ToUser1("SecondMessage", messageHandlerUser1);
		user1ToUser2("ThirdMessage", messageHandlerUser2);
		user2ToUser1("FourthMessage", messageHandlerUser1);
		user2ToUser1("FiftMessage", messageHandlerUser1);
	}

	/**
	 * Send meassage and wait for response.
	 * 
	 * @param connectionThreadSender
	 *            the connection thread sender
	 * @param messageHandlerUserDestination
	 *            the message handler user destination
	 * @param sendedMessage
	 *            the sended message
	 * @return the iRC message
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private IRCMessage sendMessageAndWaitForResponse(
			final ConnectionHandler connectionThreadSender,
			final SimpleMessageHandler messageHandlerUserDestination,
			final IRCMessage sendedMessage) throws InterruptedException {
		messageHandlerUserDestination.reset();
		sendMessage(connectionThreadSender, sendedMessage);
		waitForMessageInHandler(messageHandlerUserDestination);
		final IRCMessage receivedMessage = messageHandlerUserDestination
				.getLastReceivedMessage();
		assertNotNull(receivedMessage);
		return receivedMessage;
	}

	/**
	 * Send message to global.
	 * 
	 * @param userA
	 *            the user a
	 * @param connectionThreadA
	 *            the connection thread a
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUserB
	 *            the message handler user b
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void sendMessageToGlobal(final IRCUser userA,
			final ConnectionHandler connectionThreadA, final String messageStr,
			final SimpleMessageHandler messageHandlerUserB)
			throws InterruptedException {
		final IRCMessage currentSendedMessage = buildSimpleMessage(userA,
				messageStr);
		final IRCMessage receivedMessage = sendMessageAndWaitForResponse(
				connectionThreadA, messageHandlerUserB, currentSendedMessage);
		assertTrue(receivedMessage instanceof IRCMessageChat);
		final IRCMessageChat receivedChatMessage = (IRCMessageChat) receivedMessage;
		checkSimpleMessageReceive(userA, messageStr, receivedChatMessage);
	}

	/**
	 * Send private message to global.
	 * 
	 * @param userSource
	 *            the user a
	 * @param connectionThreadSource
	 *            the connection thread a
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUserDestination
	 *            the message handler user b
	 * @param toId
	 *            the to id
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void sendPrivateMessage(final IRCUser userSource,
			final ConnectionHandler connectionThreadSource,
			final String messageStr,
			final SimpleMessageHandler messageHandlerUserDestination,
			IRCUser userDestination) throws InterruptedException {
		final IRCMessage currentSendedMessage = buildSimplePrivateMessage(
				userSource, messageStr, userDestination.getId());
		final IRCMessage receivedMessage = sendMessageAndWaitForResponse(
				connectionThreadSource, messageHandlerUserDestination,
				currentSendedMessage);
		assertTrue(receivedMessage instanceof IRCMessageChatPrivate);
		final IRCMessageChatPrivate receivedChatMessage = (IRCMessageChatPrivate) receivedMessage;
		assertEquals(userDestination.getId(), receivedChatMessage.getToId());
		checkSimpleMessageReceive(userSource, messageStr, receivedChatMessage);
	}

	/**
	 * User1 send to global with2 listening.
	 * 
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUser2
	 *            the message handler user2
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void user1SendToGlobalWith2Listening(final String messageStr,
			final SimpleMessageHandler messageHandlerUser2)
			throws InterruptedException {
		sendMessageToGlobal(user1, connectionUser1, messageStr,
				messageHandlerUser2);
	}

	/**
	 * User1 send to user2.
	 * 
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUser2
	 *            the message handler user2
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void user1ToUser2(final String messageStr,
			final SimpleMessageHandler messageHandlerUser2)
			throws InterruptedException {
		sendPrivateMessage(user1, connectionUser1, messageStr,
				messageHandlerUser2, user2);
	}

	/**
	 * User2 send to global with1 listening.
	 * 
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUser1
	 *            the message handler user1
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void user2SendToGlobalWith1Listening(final String messageStr,
			final SimpleMessageHandler messageHandlerUser1)
			throws InterruptedException {
		sendMessageToGlobal(user2, connectionUser2, messageStr,
				messageHandlerUser1);
	}

	/**
	 * User2 send to user1.
	 * 
	 * @param messageStr
	 *            the message str
	 * @param messageHandlerUser1
	 *            the message handler user1
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void user2ToUser1(final String messageStr,
			final SimpleMessageHandler messageHandlerUser1)
			throws InterruptedException {
		sendPrivateMessage(user2, connectionUser2, messageStr,
				messageHandlerUser1, user1);
	}

}
