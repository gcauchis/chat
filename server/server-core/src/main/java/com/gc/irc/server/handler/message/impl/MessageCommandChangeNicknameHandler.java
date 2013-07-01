package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.command.MessageCommandChangeNickname;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;

/**
 * The Class IRCMessageCommandChangeNicknameHandler.
 */
@Component
public class MessageCommandChangeNicknameHandler extends AbstractServerCommandMessageHandler<MessageCommandChangeNickname> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(MessageCommandChangeNickname message) {
        User sender = getSender(message);
        if (sender != null) {
            getAuthenticationService().updateUserNickName(message.getFromId(), message.getNewNickname());
            sendToAllUsers(new MessageNoticeContactInfo(getAuthenticationService().getUser(message.getFromId()).getUser()));
        } else {
            getLog().warn("user doesn't exist.");
        }

    }

}
