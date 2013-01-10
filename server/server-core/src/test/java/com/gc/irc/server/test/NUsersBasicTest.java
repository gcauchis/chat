package com.gc.irc.server.test;

import org.junit.After;
import org.junit.Before;

import com.gc.irc.server.api.AbstractMultipleUserTest;

public class NUsersBasicTest extends AbstractMultipleUserTest {

    private static final int NB_USERS_CONNECTED = 6;

    /**
     * Clean.
     */
    @After
    public void clean() {
    }

    protected int getNbUserConnected() {
        return NB_USERS_CONNECTED;
    }

    /**
     * Prepare.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public void prepare() throws InterruptedException {
        // connectionUser1 = getConnectionToServer();
        // user1 = loginAndRegister(connectionUser1,
        // "TwoUserBasicTestITUser1Login", "TwoUserBasicTestITUser1Password");
        // assertNotNull(user1);
    }

}
