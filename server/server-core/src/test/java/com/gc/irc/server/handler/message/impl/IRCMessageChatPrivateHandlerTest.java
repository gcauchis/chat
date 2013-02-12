package com.gc.irc.server.handler.message.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.bridge.api.IServerBridgeProducer;
import com.gc.irc.server.bridge.api.ServerBridgeException;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest;

/**
 * The Class IRCMessageChatPrivateHandlerTest.
 */
public class IRCMessageChatPrivateHandlerTest extends AbstractIRCMessageHandlerTest<IRCMessageChatPrivateHandler, IRCMessageChatPrivate> {

    /** The server bridge producer. */
    private IServerBridgeProducer serverBridgeProducer;

    /** The users connections management. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest
     * #buildMessageInstance()
     */
    @Override
    protected IRCMessageChatPrivate buildMessageInstance() {
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
    private IRCMessageChatPrivate buildMessageInstance(final int userID, final int toId) {
        return new IRCMessageChatPrivate(userID, null, toId);
    }

    /**
     * Handle.
     */
    @Test
    public void handle() {
        final IRCUser sender = new IRCUser(1, "test1");
        final IRCUser reveiver = new IRCUser(2, "test2");
        final IRCMessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(usersConnectionsManagement.getUser(1)).andReturn(sender);
        expect(usersConnectionsManagement.getUser(2)).andReturn(reveiver);
        usersConnectionsManagement.sendTo(messageChatPrivate, 2);
        replay(usersConnectionsManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement);
    }

    /**
     * Handle receiver not exist.
     * 
     * @throws ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExist() throws ServerBridgeException {
        final IRCUser sender = new IRCUser(1, "test1");
        final IRCMessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(usersConnectionsManagement.getUser(1)).andReturn(sender);
        expect(usersConnectionsManagement.getUser(2)).andReturn(null);
        serverBridgeProducer.post(messageChatPrivate);
        replay(usersConnectionsManagement, serverBridgeProducer);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer);
    }

    /**
     * Handle receiver not exist end message stacking.
     * 
     * @throws ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExistEndMessageStacking() throws ServerBridgeException {
        getMessageHandler().setNumPassageMax(5);
        final IRCUser sender = new IRCUser(1, "test1");
        final IRCMessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        for (int i = 0; i < 5; i++) {
            messageChatPrivate.numPassage();
        }
        expect(usersConnectionsManagement.getUser(1)).andReturn(sender);
        expect(usersConnectionsManagement.getUser(2)).andReturn(null);
        replay(usersConnectionsManagement, serverBridgeProducer);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer);
    }

    /**
     * Handle receiver not exist fail to post in bridge.
     * 
     * @throws ServerBridgeException
     *             the server bridge exception
     */
    @Test
    public void handleReceiverNotExistFailToPostInBridge() throws ServerBridgeException {
        final IRCUser sender = new IRCUser(1, "test1");
        final IRCMessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(usersConnectionsManagement.getUser(1)).andReturn(sender);
        expect(usersConnectionsManagement.getUser(2)).andReturn(null);
        serverBridgeProducer.post(messageChatPrivate);
        expectLastCall().andThrow(new ServerBridgeException());
        replay(usersConnectionsManagement, serverBridgeProducer);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement, serverBridgeProducer);
    }

    /**
     * Handle sender not exist.
     */
    @Test
    public void handleSenderNotExist() {
        final IRCMessageChatPrivate messageChatPrivate = buildMessageInstance(1, 2);
        expect(usersConnectionsManagement.getUser(1)).andReturn(null);
        replay(usersConnectionsManagement);

        getMessageHandler().handle(messageChatPrivate);

        verify(usersConnectionsManagement);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        final IRCMessageChatPrivateHandler ircMessageChatPrivateHandler = new IRCMessageChatPrivateHandler();
        usersConnectionsManagement = createMock(IUsersConnectionsManagement.class);
        ircMessageChatPrivateHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        serverBridgeProducer = createMock(IServerBridgeProducer.class);
        ircMessageChatPrivateHandler.setServerBridgeProducer(serverBridgeProducer);
        setMessageHandler(ircMessageChatPrivateHandler);
    }

}
