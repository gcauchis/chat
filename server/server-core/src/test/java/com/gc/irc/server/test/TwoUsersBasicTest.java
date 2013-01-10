package com.gc.irc.server.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.api.AbstractMultipleUserTest;
import com.gc.irc.server.test.handler.IMessageHandlerTester;
import com.gc.irc.server.test.handler.SimpleMessageHandler;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

/**
 * The Class TwoUserBasicTestIT.
 */
public class TwoUsersBasicTest extends AbstractMultipleUserTest {

    /** The context user1. */
    private UserContextEntity contextUser1;

    /** The context user2. */
    private UserContextEntity contextUser2;

    /**
     * Clean.
     */
    @After
    public void clean() {
        contextUser1.disconnect();
        contextUser2.disconnect();
    }

    /**
     * Basic test.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void globalConvers() throws InterruptedException {
        final SimpleMessageHandler messageHandlerUser1 = new SimpleMessageHandler();
        contextUser1.setMessageHandler(messageHandlerUser1);
        final SimpleMessageHandler messageHandlerUser2 = new SimpleMessageHandler();
        contextUser2.setMessageHandler(messageHandlerUser2);
        user1SendToGlobalWith2Listening("FisrtMessage", messageHandlerUser2);
        user2SendToGlobalWith1Listening("SecondMessage", messageHandlerUser1);
        user1SendToGlobalWith2Listening("ThirdMessage", messageHandlerUser2);
        user2SendToGlobalWith1Listening("FourthMessage", messageHandlerUser1);
        user2SendToGlobalWith1Listening("FiftMessage", messageHandlerUser1);
    }

    /**
     * Prepare.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public void prepare() throws InterruptedException {
        contextUser1 = getConnectedUser("TwoUserBasicTestITUser1Login", "TwoUserBasicTestITUser1Password");
        contextUser2 = getConnectedUser("TwoUserBasicTestITUser2Login", "TwoUserBasicTestITUser2Password");
    }

    /**
     * Private convers.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void privateConvers() throws InterruptedException {
        final SimpleMessageHandler messageHandlerUser1 = new SimpleMessageHandler();
        contextUser1.setMessageHandler(messageHandlerUser1);
        final SimpleMessageHandler messageHandlerUser2 = new SimpleMessageHandler();
        contextUser2.setMessageHandler(messageHandlerUser2);
        user1ToUser2("FisrtMessage", messageHandlerUser2);
        user2ToUser1("SecondMessage", messageHandlerUser1);
        user1ToUser2("ThirdMessage", messageHandlerUser2);
        user2ToUser1("FourthMessage", messageHandlerUser1);
        user2ToUser1("FiftMessage", messageHandlerUser1);
    }

    /**
     * Send message to global.
     * 
     * @param userA
     *            the user a
     * @param connectionThreadA
     *            the connection thread a
     * @param messageStr
     *            the message str
     * @param messageHandlerUserB
     *            the message handler user b
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void sendMessageToGlobal(final IRCUser userA, final ConnectionHandler connectionThreadA, final String messageStr,
            final IMessageHandlerTester messageHandlerUserB) throws InterruptedException {
        sendMessageToGlobal(userA, connectionThreadA, messageStr, Arrays.asList(messageHandlerUserB));

    }

    /**
     * Send private message to global.
     * 
     * @param userSource
     *            the user a
     * @param connectionThreadSource
     *            the connection thread a
     * @param messageStr
     *            the message str
     * @param messageHandlerUserDestination
     *            the message handler user b
     * @param userDestination
     *            the user destination
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void sendPrivateMessage(final IRCUser userSource, final ConnectionHandler connectionThreadSource, final String messageStr,
            final IMessageHandlerTester messageHandlerUserDestination, final IRCUser userDestination) throws InterruptedException {
        sendPrivateMessage(userSource, connectionThreadSource, messageStr, messageHandlerUserDestination, userDestination, null);
    }

    /**
     * User1 send to global with2 listening.
     * 
     * @param messageStr
     *            the message str
     * @param messageHandlerUser2
     *            the message handler user2
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void user1SendToGlobalWith2Listening(final String messageStr, final SimpleMessageHandler messageHandlerUser2) throws InterruptedException {
        sendMessageToGlobal(contextUser1.getUser(), contextUser1.getConnectionUser(), messageStr, messageHandlerUser2);
    }

    /**
     * User1 send to user2.
     * 
     * @param messageStr
     *            the message str
     * @param messageHandlerUser2
     *            the message handler user2
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void user1ToUser2(final String messageStr, final SimpleMessageHandler messageHandlerUser2) throws InterruptedException {
        sendPrivateMessage(contextUser1.getUser(), contextUser1.getConnectionUser(), messageStr, messageHandlerUser2, contextUser2.getUser());
    }

    /**
     * User2 send to global with1 listening.
     * 
     * @param messageStr
     *            the message str
     * @param messageHandlerUser1
     *            the message handler user1
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void user2SendToGlobalWith1Listening(final String messageStr, final SimpleMessageHandler messageHandlerUser1) throws InterruptedException {
        sendMessageToGlobal(contextUser2.getUser(), contextUser2.getConnectionUser(), messageStr, messageHandlerUser1);
    }

    /**
     * User2 send to user1.
     * 
     * @param messageStr
     *            the message str
     * @param messageHandlerUser1
     *            the message handler user1
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void user2ToUser1(final String messageStr, final SimpleMessageHandler messageHandlerUser1) throws InterruptedException {
        sendPrivateMessage(contextUser2.getUser(), contextUser2.getConnectionUser(), messageStr, messageHandlerUser1, contextUser1.getUser());
    }

}
