package com.gc.irc.server.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.message.api.IClientMessageLine;
import com.gc.irc.common.message.impl.BasicClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.test.handler.IMessageHandlerTester;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

public abstract class AbstractMultipleUserTest extends AbstractServerTest {

    /**
     * Builds the simple message.
     * 
     * @param user
     *            the user
     * @param message
     *            the message
     * @return the iRC message
     */
    protected final IRCMessage buildSimpleMessage(final IRCUser user, final String message) {
        return new IRCMessageChat(user.getId(), Arrays.asList((IClientMessageLine) new BasicClientMessageLine(message)));
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
    protected final IRCMessage buildSimplePrivateMessage(final IRCUser user, final String message, final int toId) {
        return new IRCMessageChatPrivate(user.getId(), Arrays.asList((IClientMessageLine) new BasicClientMessageLine(message)), toId);
    }

    /**
     * Check message receive.
     * 
     * @param userSources
     *            the user sources
     * @param messageStr
     *            the message str
     * @param receivedChatMessage
     *            the received chat message
     */
    protected final void checkMessageReceived(final IRCUser userSources, final String messageStr, final IRCMessageChat receivedChatMessage) {
        checkMessageReceived(userSources, messageStr, Arrays.asList(receivedChatMessage));
    }

    /**
     * Check message received.
     * 
     * @param userSources
     *            the user sources
     * @param messageStr
     *            the message str
     * @param receivedChatMessages
     *            the received chat messages
     */
    protected final void checkMessageReceived(final IRCUser userSources, final String messageStr, final List<IRCMessageChat> receivedChatMessages) {
        for (final IRCMessageChat receivedChatMessage : receivedChatMessages) {
            assertEquals(userSources.getId(), receivedChatMessage.getFromId());
            assertNotNull(receivedChatMessage.getLines());
            assertEquals(1, receivedChatMessage.getLines().size());
            final IClientMessageLine clientMessageLine = receivedChatMessage.getLines().get(0);
            assertNotNull(clientMessageLine);
            assertTrue(clientMessageLine instanceof BasicClientMessageLine);
            final BasicClientMessageLine basicClientMessageLine = (BasicClientMessageLine) clientMessageLine;
            assertEquals(messageStr, basicClientMessageLine.getMessage());
        }
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
    protected final IRCMessage sendMessageAndWaitForResponse(final ConnectionHandler connectionThreadSender,
            final IMessageHandlerTester messageHandlerUserDestination, final IRCMessage sendedMessage) throws InterruptedException {
        return sendMessageAndWaitForResponse(connectionThreadSender, Arrays.asList(messageHandlerUserDestination), sendedMessage).get(0);
    }

    /**
     * Send message and wait for response.
     * 
     * @param connectionThreadSender
     *            the connection thread sender
     * @param messageHandlerUserDestination
     *            the message handler user destination
     * @param sendedMessage
     *            the sended message
     * @return the list
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final List<IRCMessage> sendMessageAndWaitForResponse(final ConnectionHandler connectionThreadSender,
            final List<IMessageHandlerTester> messageHandlerUserDestination, final IRCMessage sendedMessage) throws InterruptedException {

        if (messageHandlerUserDestination == null || messageHandlerUserDestination.isEmpty()) {
            return null;
        }
        for (final IMessageHandlerTester messageHandler : messageHandlerUserDestination) {
            messageHandler.reset();
        }
        sendMessage(connectionThreadSender, sendedMessage);
        final List<IRCMessage> receivedMessages = new ArrayList<IRCMessage>();
        for (final IMessageHandlerTester messageHandler : messageHandlerUserDestination) {
            waitForMessageInHandler(messageHandler);
            final IRCMessage receivedMessage = messageHandler.getLastReceivedMessage();
            assertNotNull(receivedMessage);
            receivedMessages.add(receivedMessage);
        }

        return receivedMessages;
    }

    /**
     * Send message to global.
     * 
     * @param userSrc
     *            the user src
     * @param connectionThreadSrc
     *            the connection thread src
     * @param messageStr
     *            the message str
     * @param messageHandlerUserDest
     *            the message handler user dest
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void sendMessageToGlobal(final IRCUser userSrc, final ConnectionHandler connectionThreadSrc, final String messageStr,
            final List<IMessageHandlerTester> messageHandlerUserDest) throws InterruptedException {
        final IRCMessage currentSendedMessage = buildSimpleMessage(userSrc, messageStr);
        final List<IRCMessage> receivedMessages = sendMessageAndWaitForResponse(connectionThreadSrc, messageHandlerUserDest, currentSendedMessage);
        final List<IRCMessageChat> receivedMessagesToCheck = new ArrayList<IRCMessageChat>();
        for (final IRCMessage receivedMessage : receivedMessages) {
            assertTrue("" + receivedMessage, receivedMessage instanceof IRCMessageChat);
            receivedMessagesToCheck.add((IRCMessageChat) receivedMessage);
        }
        assertEquals(messageHandlerUserDest.size(), receivedMessagesToCheck.size());
        checkMessageReceived(userSrc, messageStr, receivedMessagesToCheck);
    }

    /**
     * Send message to global.
     * 
     * @param contextSrc
     *            the context src
     * @param messageStr
     *            the message str
     * @param contextsDes
     *            the contexts des
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void sendMessageToGlobal(final UserContextEntity contextSrc, final String messageStr, final List<UserContextEntity> contextsDest)
            throws InterruptedException {

        final List<IMessageHandlerTester> messageHandlerUserDest = new ArrayList<IMessageHandlerTester>();
        if (contextsDest != null && !contextsDest.isEmpty()) {
            for (final UserContextEntity contextEntity : contextsDest) {
                messageHandlerUserDest.add(contextEntity.getMessageHandler());
            }
        }
        sendMessageToGlobal(contextSrc.getUser(), contextSrc.getConnectionUser(), messageStr, messageHandlerUserDest);

    }

    /**
     * Send private message.
     * 
     * @param userSource
     *            the user source
     * @param connectionThreadSource
     *            the connection thread source
     * @param messageStr
     *            the message str
     * @param messageHandlerUserDestination
     *            the message handler user destination
     * @param userDestination
     *            the user destination
     * @param otherUsersMessageHandlers
     *            the other users message handlers
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void sendPrivateMessage(final IRCUser userSource, final ConnectionHandler connectionThreadSource, final String messageStr,
            final IMessageHandlerTester messageHandlerUserDestination, final IRCUser userDestination,
            final List<IMessageHandlerTester> otherUsersMessageHandlers) throws InterruptedException {
        final IRCMessage currentSendedMessage = buildSimplePrivateMessage(userSource, messageStr, userDestination.getId());
        if (otherUsersMessageHandlers != null && !otherUsersMessageHandlers.isEmpty()) {
            for (final IMessageHandlerTester otherMessageHandler : otherUsersMessageHandlers) {
                otherMessageHandler.reset();
            }
        }
        final IRCMessage receivedMessage = sendMessageAndWaitForResponse(connectionThreadSource, messageHandlerUserDestination, currentSendedMessage);
        assertTrue("" + receivedMessage, receivedMessage instanceof IRCMessageChatPrivate);
        final IRCMessageChatPrivate receivedChatMessage = (IRCMessageChatPrivate) receivedMessage;
        assertEquals(userDestination.getId(), receivedChatMessage.getToId());
        checkMessageReceived(userSource, messageStr, receivedChatMessage);
        // wait and verify no private massage to other users
        Thread.sleep(500);
        if (otherUsersMessageHandlers != null && !otherUsersMessageHandlers.isEmpty()) {
            for (final IMessageHandlerTester otherMessageHandler : otherUsersMessageHandlers) {
                assertFalse(otherMessageHandler.isMessageRecieved());
            }
        }
    }

    /**
     * Send private message.
     * 
     * @param contextSrc
     *            the context src
     * @param messageStr
     *            the message str
     * @param contextDest
     *            the context dest
     * @param otherUsersContexts
     *            the other users contexts
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void sendPrivateMessage(final UserContextEntity contextSrc, final String messageStr, final UserContextEntity contextDest,
            final List<UserContextEntity> otherUsersContexts) throws InterruptedException {
        final List<IMessageHandlerTester> otherUsersMessageHandlers = new ArrayList<IMessageHandlerTester>();
        if (otherUsersContexts != null && !otherUsersContexts.isEmpty()) {
            for (final UserContextEntity contextEntity : otherUsersContexts) {
                otherUsersMessageHandlers.add(contextEntity.getMessageHandler());
            }
        }
        sendPrivateMessage(contextSrc.getUser(), contextSrc.getConnectionUser(), messageStr, contextDest.getMessageHandler(), contextDest.getUser(),
                otherUsersMessageHandlers);
    }

}
