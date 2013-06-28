package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler;

/**
 * The Class IRCMessageChatHandler.
 */
@Component
public class MessageChatHandler extends AbstractServerMessageHandler<MessageChat> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#
     * internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(final MessageChat message) {
        final IRCUser sender = getSender(message);
        if (sender == null) {
            getLog().warn("inexisting sender id");
        } else {
            getLog().debug("Global message form {}", sender.getNickName());
            sendToAllUsers(message);
        }

    }

}
