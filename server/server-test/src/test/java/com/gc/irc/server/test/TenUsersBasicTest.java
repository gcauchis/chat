package com.gc.irc.server.test;

import com.gc.irc.server.AbstractNUsersBasicTest;

/**
 * The Class TenUsersBasicTest.
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public class TenUsersBasicTest extends AbstractNUsersBasicTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbMessageToSend()
	 */
	/** {@inheritDoc} */
	@Override
	protected int getNbMessageToSend() {
		return 10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbUserConnected()
	 */
	/** {@inheritDoc} */
	@Override
	protected int getNbUserConnected() {
		return 10;
	}

}
