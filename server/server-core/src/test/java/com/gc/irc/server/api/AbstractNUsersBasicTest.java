package com.gc.irc.server.api;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.server.test.handler.SimpleMessageHandler;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

/**
 * The Class NUsersBasicTest.
 */
public abstract class AbstractNUsersBasicTest extends AbstractMultipleUserTest {

    /** The Constant NB_MESSAGE_TO_SEND. */
    private static final int NB_MESSAGE_TO_SEND = 5;

    /** The Constant NB_USERS_CONNECTED. */
    private static final int NB_USERS_CONNECTED = 6;

    /** The contexts. */
    private List<UserContextEntity> contexts;

    /** The random. */
    private final Random random = new SecureRandom();

    private long testId;

    /**
     * Clean.
     */
    @After
    public final void clean() {
        for (UserContextEntity context : contexts) {
            context.cleanMessageHandler();
            context.disconnect();
        }
        contexts.clear();
        contexts = null;
    }

    /**
     * Gets the context.
     * 
     * @param index
     *            the c
     * @return the context
     */
    protected final UserContextEntity getContext(int index) {
        if (index < 0 || index >= getNbUserConnected()) {
            getLog().error("Invalid index for get context {} of {}", index, getNbUserConnected());
            fail();
        }
        return contexts.get(index);
    }

    /**
     * Gets the context list without.
     * 
     * @param asList
     *            the as list
     * @return the context list without
     */
    private List<UserContextEntity> getContextListWithout(List<UserContextEntity> contextToRemove) {
        List<UserContextEntity> result = getContexts();
        result.removeAll(contextToRemove);
        return result;
    }

    /**
     * Gets the context list without.
     * 
     * @param contextSender
     *            the context sender
     * @return the context list without
     */
    private List<UserContextEntity> getContextListWithout(UserContextEntity... contextSender) {
        return getContextListWithout(Arrays.asList(contextSender));
    }

    /**
     * Gets the contexts.
     * 
     * @return the contexts
     */
    protected final List<UserContextEntity> getContexts() {
        return new ArrayList<UserContextEntity>(contexts);
    }

    /**
     * Gets the nb message to send.
     * 
     * @return the nb message to send
     */
    protected int getNbMessageToSend() {
        return NB_MESSAGE_TO_SEND;
    }

    /**
     * Gets the nb user connected.
     * 
     * @return the nb user connected
     */
    protected int getNbUserConnected() {
        return NB_USERS_CONNECTED;
    }

    /**
     * Gets the random user context.
     * 
     * @return the random user context
     */
    protected final UserContextEntity getRandomUserContext() {
        return getContext(getRandomUserContextIndex());
    }

    /**
     * Gets the random user context index.
     * 
     * @return the random user context index
     */
    protected final int getRandomUserContextIndex() {
        return random.nextInt(getNbUserConnected());
    }

    /**
     * Global convers.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public final void globalConvers() throws InterruptedException {
        for (int i = 0; i < getNbMessageToSend(); i++) {
            randomUserSendMessageToGlobal("global message " + i + " " + testId);
        }
    }

    /**
     * Prepare.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Before
    public final void prepare() throws InterruptedException {
        assertTrue(getNbUserConnected() > 1);
        contexts = new ArrayList<UserContextEntity>();
        testId = Math.round(Math.random() * System.currentTimeMillis());
        for (int i = 0; i < getNbUserConnected(); i++) {
            contexts.add(getConnectedUser("User" + i + "Login" + testId, "User" + i + "Password" + testId));
        }
        putNewSimpleMessageHandlerInAllContext();
    }

    @Test
    public final void privateConvers() throws InterruptedException {
        for (int i = 0; i < getNbMessageToSend(); i++) {
            randomUserSendMessageToRandomOtherOne("private message " + i + " " + testId);
        }
    }

    /**
     * Put new simple message handler in all context.
     */
    protected final void putNewSimpleMessageHandlerInAllContext() {
        for (UserContextEntity context : contexts) {
            context.setMessageHandler(new SimpleMessageHandler());
        }
    }

    /**
     * Random user send message to global.
     * 
     * @param messageStr
     *            the message str
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void randomUserSendMessageToGlobal(String messageStr) throws InterruptedException {
        userSendMessageToGlobal(getRandomUserContext(), messageStr);
    }

    /**
     * Random user send message to random other one.
     * 
     * @param messageStr
     *            the message str
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void randomUserSendMessageToRandomOtherOne(String messageStr) throws InterruptedException {
        UserContextEntity sender = getRandomUserContext();
        UserContextEntity receiver = null;
        do {
            receiver = getRandomUserContext();
        } while (sender == receiver);
        userSendMessageToOtherOne(sender, messageStr, receiver);
    }

    /**
     * User send message to global.
     * 
     * @param contextSender
     *            the context sender
     * @param messageStr
     *            the message str
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void userSendMessageToGlobal(UserContextEntity contextSender, String messageStr) throws InterruptedException {
        List<UserContextEntity> contextsDest = getContextListWithout(contextSender);
        sendMessageToGlobal(contextSender, messageStr, contextsDest);
    }

    /**
     * User send message to other one.
     * 
     * @param contextSender
     *            the context sender
     * @param messageStr
     *            the message str
     * @param contextDest
     *            the context dest
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void userSendMessageToOtherOne(UserContextEntity contextSender, String messageStr, UserContextEntity contextDest)
            throws InterruptedException {
        assertNotSame(contextSender, contextDest);
        List<UserContextEntity> contextsDest = getContextListWithout(contextSender, contextDest);
        sendPrivateMessage(contextSender, messageStr, contextDest, contextsDest);
    }

}
