package com.gc.irc.server.handler.message;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.chat.MessageChatPrivate;
import com.gc.irc.server.bridge.IServerBridgeProducer;
import com.gc.irc.server.bridge.ServerBridgeException;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UsersConnectionsManagement;
import com.gc.irc.server.handler.message.MessageChatPrivateHandler;
import com.gc.irc.server.handler.message.test.AbstractMessageHandlerTest;

/**
 * The Class IRCMessageChatPrivateHandlerTest.
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public class MessageChatPrivateHandlerTest extends AbstractMessageHandlerTest<MessageChatPrivateHandler, MessageChatPrivate> {

    /** The server bridge producer. */
    private IServerBridgeProducer serverBridgeProducer;

    /** The users connections management. */
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The user management */
    private UserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest
     * #buildMessageInstance()
     */
    /** {@inheritDoc} */
    @Override
    protected MessageChatPrivate buildMessageInstance() {
        return buildMessageInstance(1, 2);
    }

    /**
     * Builds the message instance.
     * 
     * @param userID
     *            the user id
     * @param toId
     *            the to id
     * @return the iRC message chat private
     */
    private MessageChatPrivate buildMessageInstance(final int userID, final int toId) {
        return new MessageChatPrivate(userID, null, toId);
    }

    /**
     * Handle.
     */
    @Test
    public void handle() {
        final User sender = new User(1, "test1");
        final User reveiver = new User(2, "test2");
        final MessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(userManagement.getUser(1)).andReturn(sender);
        expect(userManagement.getUser(2)).andReturn(reveiver);
        usersConnectionsManagement.sendTo(messageChatPrivate, 2);
        replay(usersConnectionsManagement, userManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, userManagement);
    }

    /**
     * Handle receiver not exist.
     *
     * @throws com.gc.irc.server.bridge.ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExist() throws ServerBridgeException {
        final User sender = new User(1, "test1");
        final MessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(userManagement.getUser(1)).andReturn(sender);
        expect(userManagement.getUser(2)).andReturn(null);
        serverBridgeProducer.post(messageChatPrivate);
        replay(usersConnectionsManagement, serverBridgeProducer, userManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer, userManagement);
    }

    /**
     * Handle receiver not exist end message stacking.
     *
     * @throws com.gc.irc.server.bridge.ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExistEndMessageStacking() throws ServerBridgeException {
        getMessageHandler().setNumPassageMax(5);
        final User sender = new User(1, "test1");
        final MessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        for (int i = 0; i < 5; i++) {
            messageChatPrivate.numPassage();
        }
        expect(userManagement.getUser(1)).andReturn(sender);
        expect(userManagement.getUser(2)).andReturn(null);
        replay(usersConnectionsManagement, serverBridgeProducer, userManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer, userManagement);
    }

    /**
     * Handle receiver not exist fail to post in bridge.
     *
     * @throws com.gc.irc.server.bridge.ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExistFailToPostInBridge() throws ServerBridgeException {
        final User sender = new User(1, "test1");
        final MessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(userManagement.getUser(1)).andReturn(sender);
        expect(userManagement.getUser(2)).andReturn(null);
        serverBridgeProducer.post(messageChatPrivate);
        expectLastCall().andThrow(new ServerBridgeException());
        replay(usersConnectionsManagement, serverBridgeProducer, userManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer, userManagement);
    }

    /**
     * Handle sender not exist.
     */
    @Test
    public void handleSenderNotExist() {
        final MessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(userManagement.getUser(1)).andReturn(null);
        replay(userManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(userManagement);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        final MessageChatPrivateHandler ircMessageChatPrivateHandler = new MessageChatPrivateHandler();
        usersConnectionsManagement = createMock(UsersConnectionsManagement.class);
        ircMessageChatPrivateHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        serverBridgeProducer = createMock(IServerBridgeProducer.class);
        ircMessageChatPrivateHandler.setServerBridgeProducer(serverBridgeProducer);
        userManagement = createMock(UserManagement.class);
        ircMessageChatPrivateHandler.setUserManagement(userManagement);
        setMessageHandler(ircMessageChatPrivateHandler);
    }

}
