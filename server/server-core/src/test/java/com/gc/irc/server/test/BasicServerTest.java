package com.gc.irc.server.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.message.BasicClientMessageLine;
import com.gc.irc.common.message.IClientMessageLine;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.chat.MessageChat;
import com.gc.irc.server.AbstractServerTest;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

/**
 * The Class ServerTest.
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
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
    private Message getBasicMessage() {
        return new MessageChat(0, Arrays.asList((IClientMessageLine) new BasicClientMessageLine("message")));
    }

    /**
     * Login0.
     *
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    @Test
    public void login0() throws InterruptedException {
    	User user = loginAndRegister(connectionThread, "test", "test");
    	assertNotNull(user);
    	removeTestUser(user, connectionThread);
    }

    /**
     * Login1.
     *
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    @Test
    @Ignore
    public void login1() throws InterruptedException {
    	User user = loginAndRegister(connectionThread, "test1", "test1");
    	assertNotNull(user);
    	removeTestUser(user, connectionThread);
    }

    /**
     * Login2.
     *
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    @Test
    @Ignore
    public void login2() throws InterruptedException {
    	User user = loginAndRegister(connectionThread, "test2", "test2");
    	assertNotNull(user);
    	removeTestUser(user, connectionThread);
    }

    /**
     * Login rand.
     *
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    @Test
    public void loginRand() throws InterruptedException {
        String login = "TestUser" + Math.round(Math.random() * System.currentTimeMillis());
		String password = "TestPassword" + Math.round(Math.random() * System.currentTimeMillis());
		assertNull("User should not exit already",login(connectionThread, login, password));
		assertNotNull("User should be registered",register(connectionThread, login, password));
		connectionThread.disconnect();
		
		connectionThread = getConnectionToServer();
		assertNull("User should not register again",register(connectionThread, login, password));
		connectionThread.disconnect();
		
		UserContextEntity contextEntity = getConnectedUser(login, password);
		assertNotNull("User should be abble to log", contextEntity.getUser());
		removeTestUser(contextEntity);
		contextEntity.disconnect();
		
		connectionThread = getConnectionToServer();
		assertNull("User is deleted",login(connectionThread, login, password));
    }

    /**
     * Prepare.
     *
     * @throws java.lang.InterruptedException
     *             the interrupted exception
     */
    @Before
    public void prepare() throws InterruptedException {
        connectionThread = getConnectionToServer();
    }

    /**
     * Basic test.
     *
     * @throws java.lang.InterruptedException
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
