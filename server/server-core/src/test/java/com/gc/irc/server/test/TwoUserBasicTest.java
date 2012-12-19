package com.gc.irc.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.message.api.IClientMessageLine;
import com.gc.irc.common.message.impl.BasicClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.server.api.AbstractServerIT;
import com.gc.irc.server.test.handler.SimpleMessageHandler;

/**
 * The Class TwoUserBasicTestIT.
 */
public class TwoUserBasicTest extends AbstractServerIT {

    /** The connection user1. */
    private ConnectionThread connectionUser1;

    /** The user1. */
    private IRCUser user1;

    /** The connection user2. */
    private ConnectionThread connectionUser2;

    /** The user2. */
    private IRCUser user2;

    /**
     * Prepare.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public void prepare() throws InterruptedException {
        connectionUser1 = getConnectionToServer();
        user1 = loginAndRegister(connectionUser1, "TwoUserBasicTestITUser1Login", "TwoUserBasicTestITUser1Password");
        assertNotNull(user1);
        connectionUser2 = getConnectionToServer();
        user2 = loginAndRegister(connectionUser2, "TwoUserBasicTestITUser2Login", "TwoUserBasicTestITUser2Password");
        assertNotNull(user2);
    }

    /**
     * Clean.
     */
    @After
    public void clean() {
        connectionUser1.disconnect();
        connectionUser1.interrupt();
        connectionUser2.disconnect();
        connectionUser2.interrupt();
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
     * User1 send to global with2 listening.
     * 
     * @param messageStr
     *            the message str
     * @param messageHandlerUser2
     *            the message handler user2
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void user1SendToGlobalWith2Listening(final String messageStr, final SimpleMessageHandler messageHandlerUser2) throws InterruptedException {
        sendMessageToGlobal(user1, connectionUser1, messageStr, messageHandlerUser2);
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
    private void user2SendToGlobalWith1Listening(final String messageStr, final SimpleMessageHandler messageHandlerUser1) throws InterruptedException {
        sendMessageToGlobal(user2, connectionUser2, messageStr, messageHandlerUser1);
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
    private void sendMessageToGlobal(final IRCUser userA, final ConnectionThread connectionThreadA, final String messageStr,
            final SimpleMessageHandler messageHandlerUserB) throws InterruptedException {
        final IRCMessage currentSendedMessage = buildSimpleMessage(userA, messageStr);
        messageHandlerUserB.reset();
        sendMessage(connectionThreadA, currentSendedMessage);
        waitForMessageInHandler(messageHandlerUserB);
        final IRCMessage receivedMessage = messageHandlerUserB.getLastReceivedMessage();
        assertNotNull(receivedMessage);
        assertTrue(receivedMessage instanceof IRCMessageChat);
        final IRCMessageChat receivedChatMessage = (IRCMessageChat) receivedMessage;
        assertNotNull(receivedChatMessage.getLines());
        assertEquals(1, receivedChatMessage.getLines().size());
        final IClientMessageLine clientMessageLine = receivedChatMessage.getLines().get(0);
        assertNotNull(clientMessageLine);
        assertTrue(clientMessageLine instanceof BasicClientMessageLine);
        final BasicClientMessageLine basicClientMessageLine = (BasicClientMessageLine) clientMessageLine;
        assertEquals(messageStr, basicClientMessageLine.getMessage());
    }

    /**
     * Builds the simple message.
     * 
     * @param user
     *            the user
     * @param message
     *            the message
     * @return the iRC message
     */
    private IRCMessage buildSimpleMessage(final IRCUser user, final String message) {
        return new IRCMessageChat(user.getId(), Arrays.asList((IClientMessageLine) new BasicClientMessageLine(message)));
    }

}
