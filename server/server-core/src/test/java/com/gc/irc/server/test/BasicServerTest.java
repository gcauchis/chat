package com.gc.irc.server.test;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.message.api.IClientMessageLine;
import com.gc.irc.common.message.impl.BasicClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.server.api.AbstractServerTest;

/**
 * The Class ServerTest.
 */
public class BasicServerTest extends AbstractServerTest {

    /** The connection thread. */
    private ConnectionHandler connectionThread;

    /**
     * Clean.
     */
    @After
    public void clean() {
        connectionThread.disconnect();
    }

    /**
     * Gets the basic message.
     * 
     * @return the basic message
     */
    private IRCMessage getBasicMessage() {
        return new IRCMessageChat(0, Arrays.asList((IClientMessageLine) new BasicClientMessageLine("message")));
    }

    /**
     * Login0.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void login0() throws InterruptedException {
        assertNotNull(loginAndRegister(connectionThread, "test", "test"));
    }

    /**
     * Login1.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void login1() throws InterruptedException {
        assertNotNull(loginAndRegister(connectionThread, "test1", "test"));
    }

    /**
     * Login2.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void login2() throws InterruptedException {
        assertNotNull(loginAndRegister(connectionThread, "test2", "test"));
    }

    /**
     * Login rand.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void loginRand() throws InterruptedException {
        assertNotNull(loginAndRegister(connectionThread, "TestUser" + Math.round(Math.random() * System.currentTimeMillis()), "TestPassword"
                + Math.round(Math.random() * System.currentTimeMillis())));
    }

    /**
     * Prepare.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public void prepare() throws InterruptedException {
        connectionThread = getConnectionToServer();
    }

    /**
     * Basic test.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void sendMsg() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            getLog().info("send msg");
            sendMessage(connectionThread, getBasicMessage());
            Thread.sleep(500);
        }
    }
}
