package com.gc.irc.server.core.user.management;

import java.io.ObjectOutputStream;

public interface IUserPicturesManagement {
	
	void sendUsersPictures(final ObjectOutputStream outObject);

}
