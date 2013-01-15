package com.gc.irc.server.test;

import org.junit.Ignore;

import com.gc.irc.server.api.AbstractNUsersBasicTest;

/**
 * The Class TwentyUsersBasicTest.
 */
@Ignore
public class TwentyUsersBasicTest extends AbstractNUsersBasicTest {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbMessageToSend()
     */
    @Override
    protected int getNbMessageToSend() {
        return 25;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.api.AbstractNUsersBasicTest#getNbUserConnected()
     */
    @Override
    protected int getNbUserConnected() {
        return 20;
    }

}
