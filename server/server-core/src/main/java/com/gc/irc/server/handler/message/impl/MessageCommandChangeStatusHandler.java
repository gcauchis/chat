package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.command.MessageCommandChangeStatus;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;

/**
 * The Class IRCMessageCommandChangeStatusHandler.
 */
@Component
public class MessageCommandChangeStatusHandler extends AbstractServerCommandMessageHandler<MessageCommandChangeStatus> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(MessageCommandChangeStatus message) {
        final User user = getSender(message);
        if (user != null) {
            getLog().debug(user.getNickName() + " change status to " + message.getNewStatus());
            user.setUserStatus(message.getNewStatus());
            sendToAllUsers(new MessageNoticeContactInfo(user));
        }

    }

}
