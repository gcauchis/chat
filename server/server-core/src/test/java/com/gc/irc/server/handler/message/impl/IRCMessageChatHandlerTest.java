package com.gc.irc.server.handler.message.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest;

/**
 * The Class IRCMessageChatHandlerTest.
 */
public class IRCMessageChatHandlerTest extends AbstractIRCMessageHandlerTest<MessageChatHandler, MessageChat> {

    /** The users connections management. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest#buildMessageInstance()
     */
    @Override
    protected MessageChat buildMessageInstance() {
        return buildMessageInstance(-1);
    }

    private MessageChat buildMessageInstance(final int userID) {
        return new MessageChat(userID, null);
    }

    /**
     * Handle.
     */
    @Test
    public void handle() {
        IRCUser sender = new IRCUser(1, "test");
        MessageChat messageChat = buildMessageInstance(1);
        expect(usersConnectionsManagement.getUser(1)).andReturn(sender);
        usersConnectionsManagement.sendMessageToAllUsers(messageChat);
        replay(usersConnectionsManagement);

        getMessageHandler().handle(messageChat);

        verify(usersConnectionsManagement);
    }

    /**
     * Handle user not exit.
     */
    @Test
    public void handleUserNotExit() {
        MessageChat messageChat = buildMessageInstance(1);
        expect(usersConnectionsManagement.getUser(1)).andReturn(null);
        replay(usersConnectionsManagement);

        getMessageHandler().handle(messageChat);

        verify(usersConnectionsManagement);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        MessageChatHandler ircMessageChatHandler = new MessageChatHandler();
        usersConnectionsManagement = createMock(IUsersConnectionsManagement.class);
        ircMessageChatHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        setMessageHandler(ircMessageChatHandler);
    }

}
