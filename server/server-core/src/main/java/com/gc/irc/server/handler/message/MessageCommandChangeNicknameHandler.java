package com.gc.irc.server.handler.message;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.command.MessageCommandChangeNickname;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;

/**
 * The Class IRCMessageCommandChangeNicknameHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component
public class MessageCommandChangeNicknameHandler extends AbstractServerCommandMessageHandler<MessageCommandChangeNickname> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    protected void internalHandle(MessageCommandChangeNickname message) {
        User sender = getSender(message);
        if (sender != null) {
            getAuthenticationService().updateUserNickName(sender.getId(), message.getNewNickname());
            sender = getUserManagement().changeUserNickname(sender.getId(),  message.getNewNickname());
            sendToAllUsers(new MessageNoticeContactInfo(sender));
        } else {
            getLog().warn("user doesn't exist.");
        }

    }

}
