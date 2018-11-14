package com.gc.irc.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.message.BasicClientMessageLine;
import com.gc.irc.common.message.ClientMessageLine;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.common.protocol.chat.MessageChatPrivate;
import com.gc.irc.server.test.handler.MessageHandlerTester;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

/**
 * <p>Abstract AbstractMultipleUserTest class.</p>
 *
 * @version 0.0.4
 * @since 0.0.4
 */
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
    protected final Message buildSimpleMessage(final User user, final String message) {
        return new MessageChat(user.getId(), Arrays.asList((ClientMessageLine) new BasicClientMessageLine(message)));
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
    protected final Message buildSimplePrivateMessage(final User user, final String message, final long toId) {
        return new MessageChatPrivate(user.getId(), Arrays.asList((ClientMessageLine) new BasicClientMessageLine(message)), toId);
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
    protected final void checkMessageReceived(final User userSources, final String messageStr, final MessageChat receivedChatMessage) {
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
    protected final void checkMessageReceived(final User userSources, final String messageStr, final List<MessageChat> receivedChatMessages) {
        for (final MessageChat receivedChatMessage : receivedChatMessages) {
            assertEquals(userSources.getId(), receivedChatMessage.getFromId());
            assertNotNull(receivedChatMessage.getLines());
            assertEquals(1, receivedChatMessage.getLines().size());
            final ClientMessageLine clientMessageLine = receivedChatMessage.getLines().get(0);
            assertNotNull(clientMessageLine);
            assertTrue(clientMessageLine instanceof BasicClientMessageLine);
            final BasicClientMessageLine basicClientMessageLine = (BasicClientMessageLine) clientMessageLine;
            assertEquals(messageStr, basicClientMessageLine.getMessage());
        }
    }

    /**
     * Reset message handlers.
     *
     * @param messageHandlers
     *            the message handlers
     */
    protected final void resetMessageHandlers(final List<MessageHandlerTester> messageHandlers) {
        if (messageHandlers != null && !messageHandlers.isEmpty()) {
            for (final MessageHandlerTester messageHandler : messageHandlers) {
                messageHandler.reset();
            }
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    protected final Message sendMessageAndWaitForResponse(final ConnectionHandler connectionThreadSender,
            final MessageHandlerTester messageHandlerUserDestination, final Message sendedMessage) throws InterruptedException {
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    protected final List<Message> sendMessageAndWaitForResponse(final ConnectionHandler connectionThreadSender,
            final List<MessageHandlerTester> messageHandlerUserDestination, final Message sendedMessage) throws InterruptedException {

        if (messageHandlerUserDestination == null || messageHandlerUserDestination.isEmpty()) {
            return null;
        }
        resetMessageHandlers(messageHandlerUserDestination);
        sendMessage(connectionThreadSender, sendedMessage);
        final List<Message> receivedMessages = new ArrayList<Message>();
        for (final MessageHandlerTester messageHandler : messageHandlerUserDestination) {
            waitForMessageInHandler(messageHandler);
            final Message receivedMessage = messageHandler.getLastReceivedMessage();
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    protected final void sendMessageToGlobal(final User userSrc, final ConnectionHandler connectionThreadSrc, final String messageStr,
            final List<MessageHandlerTester> messageHandlerUserDest) throws InterruptedException {
        final Message currentSendedMessage = buildSimpleMessage(userSrc, messageStr);
        final List<Message> receivedMessages = sendMessageAndWaitForResponse(connectionThreadSrc, messageHandlerUserDest, currentSendedMessage);
        final List<MessageChat> receivedMessagesToCheck = new ArrayList<MessageChat>();
        for (final Message receivedMessage : receivedMessages) {
            assertTrue("" + receivedMessage, receivedMessage instanceof MessageChat);
            receivedMessagesToCheck.add((MessageChat) receivedMessage);
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     * @param contextsDest a {@link java.util.List} object.
     */
    protected final void sendMessageToGlobal(final UserContextEntity contextSrc, final String messageStr, final List<UserContextEntity> contextsDest)
            throws InterruptedException {

        final List<MessageHandlerTester> messageHandlerUserDest = new ArrayList<MessageHandlerTester>();
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    protected final void sendPrivateMessage(final User userSource, final ConnectionHandler connectionThreadSource, final String messageStr,
            final MessageHandlerTester messageHandlerUserDestination, final User userDestination,
            final List<MessageHandlerTester> otherUsersMessageHandlers) throws InterruptedException {
        final Message currentSendedMessage = buildSimplePrivateMessage(userSource, messageStr, userDestination.getId());
        resetMessageHandlers(otherUsersMessageHandlers);
        final Message receivedMessage = sendMessageAndWaitForResponse(connectionThreadSource, messageHandlerUserDestination, currentSendedMessage);
        assertTrue("" + receivedMessage, receivedMessage instanceof MessageChatPrivate);
        final MessageChatPrivate receivedChatMessage = (MessageChatPrivate) receivedMessage;
        assertEquals(userDestination.getId(), receivedChatMessage.getToId());
        checkMessageReceived(userSource, messageStr, receivedChatMessage);
        // wait and verify no private massage to other users
        Thread.sleep(500);
        if (otherUsersMessageHandlers != null && !otherUsersMessageHandlers.isEmpty()) {
            for (final MessageHandlerTester otherMessageHandler : otherUsersMessageHandlers) {
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
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    protected final void sendPrivateMessage(final UserContextEntity contextSrc, final String messageStr, final UserContextEntity contextDest,
            final List<UserContextEntity> otherUsersContexts) throws InterruptedException {
        final List<MessageHandlerTester> otherUsersMessageHandlers = new ArrayList<MessageHandlerTester>();
        if (otherUsersContexts != null && !otherUsersContexts.isEmpty()) {
            for (final UserContextEntity contextEntity : otherUsersContexts) {
                otherUsersMessageHandlers.add(contextEntity.getMessageHandler());
            }
        }
        sendPrivateMessage(contextSrc.getUser(), contextSrc.getConnectionUser(), messageStr, contextDest.getMessageHandler(), contextDest.getUser(),
                otherUsersMessageHandlers);
    }

}
