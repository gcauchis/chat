package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.command.IRCMessageCommandChangeStatus;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;

/**
 * The Class IRCMessageCommandChangeStatusHandler.
 */
@Component
public class MessageCommandChangeStatusHandler extends AbstractServerCommandMessageHandler<IRCMessageCommandChangeStatus> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(IRCMessageCommandChangeStatus message) {
        final IRCUser user = getSender(message);
        if (user != null) {
            getLog().debug(user.getNickName() + " change status to " + message.getNewStatus());
            user.setUserStatus(message.getNewStatus());
            sendToAllUsers(new IRCMessageNoticeContactInfo(user));
        }

    }

}
