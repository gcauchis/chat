package com.gc.irc.server.handler.message;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.chat.MessageChat;

/**
 * The Class IRCMessageChatHandler.
 *
 * @version 0.0.4
 * @author x472511
 */
@Component
public class MessageChatHandler extends AbstractServerMessageHandler<MessageChat> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#
     * internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    protected void internalHandle(final MessageChat message) {
        final User sender = getSender(message);
        if (sender == null) {
            getLog().warn("inexisting sender id");
        } else {
            getLog().debug("Global message form {}", sender.getNickName());
            sendToAllUsers(message);
        }

    }

}
