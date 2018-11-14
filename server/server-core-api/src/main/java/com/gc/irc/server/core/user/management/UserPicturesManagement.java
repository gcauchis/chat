package com.gc.irc.server.core.user.management;

import com.gc.irc.common.message.MessageSender;

/**
 * <p>IUserPicturesManagement interface.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
public interface UserPicturesManagement {
	
	/**
	 * <p>sendUsersPictures.</p>
	 *
	 * @param messageSender a {@link com.gc.irc.common.message.MessageSender} object.
	 */
	void sendUsersPictures(final MessageSender messageSender);

}
