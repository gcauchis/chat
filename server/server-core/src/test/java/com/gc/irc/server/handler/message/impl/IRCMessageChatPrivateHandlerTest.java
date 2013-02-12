package com.gc.irc.server.handler.message.impl;

import static org.easymock.EasyMock.createMock;

import org.junit.Before;

import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.test.api.AbstractIRCMessageHandlerTest;

public class IRCMessageChatPrivateHandlerTest extends AbstractIRCMessageHandlerTest<IRCMessageChatPrivateHandler, IRCMessageChatPrivate> {

    /** The users connections management. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    @Override
    protected IRCMessageChatPrivate buildMessageInstance() {
        return buildMessageInstance(1, 2);
    }

    private IRCMessageChatPrivate buildMessageInstance(final int userID, final int toId) {
        return new IRCMessageChatPrivate(userID, null, toId);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        final IRCMessageChatPrivateHandler ircMessageChatHandler = new IRCMessageChatPrivateHandler();
        usersConnectionsManagement = createMock(IUsersConnectionsManagement.class);
        ircMessageChatHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        setMessageHandler(ircMessageChatHandler);
    }

}
