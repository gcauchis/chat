package com.gc.irc.server.test;

import com.gc.irc.server.api.AbstractNUsersBasicTest;

/**
 * The Class TenUserBasicTest.
 */
public class TenUserBasicTest extends AbstractNUsersBasicTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbMessageToSend()
	 */
	@Override
	protected int getNbMessageToSend() {
		return 10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbUserConnected()
	 */
	@Override
	protected int getNbUserConnected() {
		return 10;
	}

}
