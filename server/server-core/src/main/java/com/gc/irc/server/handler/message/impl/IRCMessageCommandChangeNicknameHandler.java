package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.command.IRCMessageCommandChangeNickname;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;

/**
 * The Class IRCMessageCommandChangeNicknameHandler.
 */
@Component
public class IRCMessageCommandChangeNicknameHandler extends AbstractServerCommandMessageHandler<IRCMessageCommandChangeNickname> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(IRCMessageCommandChangeNickname message) {
        IRCUser sender = getSender(message);
        if (sender != null) {
            getAuthenticationService().updateUserNickName(message.getFromId(), message.getNewNickname());
            sendToAllUsers(new IRCMessageNoticeContactInfo(getAuthenticationService().getUser(message.getFromId()).getUser()));
        } else {
            getLog().warn("user doesn't exist.");
        }

    }

}
