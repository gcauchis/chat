package com.gc.irc.server.core.user.management;

import com.gc.irc.common.message.MessageSender;

/**
 * <p>IUserPicturesManagement interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface UserPicturesManagement {
	
	/**
	 * <p>sendUsersPictures.</p>
	 *
	 * @param messageSender a {@link com.gc.irc.common.message.MessageSender} object.
	 */
	void sendUsersPictures(final MessageSender messageSender);

}
