package com.gc.irc.server.test.handler.message;

import org.springframework.stereotype.Component;

import com.gc.irc.common.protocol.notice.MessageNoticeServerMessage;
import com.gc.irc.server.handler.message.AbstractServerCommandMessageHandler;
import com.gc.irc.server.test.protocol.command.MessageCommandTestDeleteUser;

@Component
/**
 * <p>MessageCommandTestDeleteUserHandler class.</p>
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public class MessageCommandTestDeleteUserHandler extends
		AbstractServerCommandMessageHandler<MessageCommandTestDeleteUser> {

	/** {@inheritDoc} */
	@Override
	protected void internalHandle(MessageCommandTestDeleteUser message) {
		getLog().info("DELETE USER "+message.getFromId());
		getAuthenticationService().delete(message.getFromId());
		sendTo(new MessageNoticeServerMessage("DELETE"+message.getFromId()), message.getFromId());
	}

}
