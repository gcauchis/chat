package com.gc.irc.server.handler.message;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.server.core.user.management.IUserManagement;
import com.gc.irc.server.core.user.management.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.MessageChatHandler;
import com.gc.irc.server.handler.message.test.AbstractMessageHandlerTest;

/**
 * The Class IRCMessageChatHandlerTest.
 */
public class MessageChatHandlerTest extends AbstractMessageHandlerTest<MessageChatHandler, MessageChat> {

    /** The users connections management. */
    private IUsersConnectionsManagement usersConnectionsManagement;
    
    /** The user management */
    private IUserManagement userManagement;

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
        User sender = new User(1, "test");
        MessageChat messageChat = buildMessageInstance(1);
        expect(userManagement.getUser(1)).andReturn(sender);
        usersConnectionsManagement.sendMessageToAllUsers(messageChat);
        replay(usersConnectionsManagement, userManagement);

        getMessageHandler().handle(messageChat);

        verify(usersConnectionsManagement, userManagement);
    }

    /**
     * Handle user not exit.
     */
    @Test
    public void handleUserNotExit() {
        MessageChat messageChat = buildMessageInstance(1);
        expect(userManagement.getUser(1)).andReturn(null);
        replay(userManagement);

        getMessageHandler().handle(messageChat);

        verify(userManagement);
    }

    /**
     * Inits the.
     */
    @Before
    public void init() {
        MessageChatHandler ircMessageChatHandler = new MessageChatHandler();
        usersConnectionsManagement = createMock(IUsersConnectionsManagement.class);
        ircMessageChatHandler.setUsersConnectionsManagement(usersConnectionsManagement);
        userManagement = createMock(IUserManagement.class);
        ircMessageChatHandler.setUserManagement(userManagement);
        setMessageHandler(ircMessageChatHandler);
    }

}
