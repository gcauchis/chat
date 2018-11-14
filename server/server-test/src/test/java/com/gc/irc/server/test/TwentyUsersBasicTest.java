package com.gc.irc.server.test;

import org.junit.Ignore;

import com.gc.irc.server.AbstractNUsersBasicTest;

/**
 * The Class TwentyUsersBasicTest.
 *
 * @version 0.0.4
 * @since 0.0.4
 */
@Ignore
public class TwentyUsersBasicTest extends AbstractNUsersBasicTest {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbMessageToSend()
     */
    /** {@inheritDoc} */
    @Override
    protected int getNbMessageToSend() {
        return 25;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbUserConnected()
     */
    /** {@inheritDoc} */
    @Override
    protected int getNbUserConnected() {
        return 20;
    }

}
