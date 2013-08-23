package com.gc.irc.server.test.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.protocol.notice.MessageNoticeServerMessage;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;
import com.gc.irc.server.test.protocol.command.MessageCommandTestDeleteUser;

@Component
public class MessageCommandTestDeleteUserHandler extends
		AbstractServerCommandMessageHandler<MessageCommandTestDeleteUser> {

	@Override
	protected void internalHandle(MessageCommandTestDeleteUser message) {
		getLog().info("DELETE USER "+message.getFromId());
		getAuthenticationService().delete(message.getFromId());
		sendTo(new MessageNoticeServerMessage("DELETE"+message.getFromId()), message.getFromId());
	}

}
