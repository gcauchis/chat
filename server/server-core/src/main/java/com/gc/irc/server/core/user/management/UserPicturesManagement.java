package com.gc.irc.server.core.user.management;

import java.io.ObjectOutputStream;

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
	 * @param outObject a {@link java.io.ObjectOutputStream} object.
	 */
	void sendUsersPictures(final ObjectOutputStream outObject);

}
