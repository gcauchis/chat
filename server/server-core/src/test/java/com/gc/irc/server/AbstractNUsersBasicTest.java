package com.gc.irc.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.gc.irc.common.entity.User;
import com.gc.irc.common.entity.UserStatus;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.command.MessageCommandChangeNickname;
import com.gc.irc.common.protocol.command.MessageCommandChangeStatus;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;
import com.gc.irc.server.test.handler.IMessageHandlerTester;
import com.gc.irc.server.test.handler.LoginContactInfoMessageHandler;
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
    protected final Random random = new SecureRandom();

    /** The test id. */
    private long testId;

    /**
     * Change nick name.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public final void changeNickName() throws InterruptedException {
        UserContextEntity senderContext = getRandomUserContext();
        User user = senderContext.getUser();
        user.setNickName("ChangeNickName-" + user.getId() + "-" + testId);
        Message changeNickNameMessage = new MessageCommandChangeNickname(user);
        List<Message> receivedMessages = sendMessageAndWaitForResponse(senderContext.getConnectionUser(),
                getMessageHandlers(getContextListWithout(senderContext)), changeNickNameMessage);
        assertEquals(getNbUserConnected() - 1, receivedMessages.size());
        for (Message receivedMessage : receivedMessages) {
            assertTrue(receivedMessage instanceof MessageNoticeContactInfo);
            MessageNoticeContactInfo message = (MessageNoticeContactInfo) receivedMessage;
            assertEquals(user.getId(), message.getFromId());
            assertEquals(user.getId(), message.getUser().getId());
            assertEquals(user.getNickName(), message.getUser().getNickName());
        }
    }

    /**
     * Change status.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public final void changeStatus() throws InterruptedException {
        UserContextEntity senderContext = getRandomUserContext();
        User user = senderContext.getUser();
        UserStatus newStatus = UserStatus.BUSY;
        changeStatus(senderContext, user, newStatus);
        newStatus = UserStatus.ABSENT;
        changeStatus(senderContext, user, newStatus);
        newStatus = UserStatus.ONLINE;
        changeStatus(senderContext, user, newStatus);
    }

    /**
     * Change status.
     * 
     * @param senderContext
     *            the sender context
     * @param user
     *            the user
     * @param newStatus
     *            the new status
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void changeStatus(UserContextEntity senderContext, User user, UserStatus newStatus) throws InterruptedException {
        Message changeStatusMessage = new MessageCommandChangeStatus(user.getId(), newStatus);
        List<Message> receivedMessages = sendMessageAndWaitForResponse(senderContext.getConnectionUser(),
                getMessageHandlers(getContextListWithout(senderContext)), changeStatusMessage);
        assertEquals(getNbUserConnected() - 1, receivedMessages.size());
        for (Message receivedMessage : receivedMessages) {
            assertTrue(receivedMessage instanceof MessageNoticeContactInfo);
            MessageNoticeContactInfo message = (MessageNoticeContactInfo) receivedMessage;
            assertEquals(user.getId(), message.getFromId());
            assertEquals(user.getId(), message.getUser().getId());
            assertEquals(newStatus, message.getUser().getUserStatus());
        }
    }

    /**
     * Clean.
     * @throws InterruptedException 
     */
    @After
    public final void clean() throws InterruptedException {
        for (UserContextEntity context : contexts) {
        	finalizeTestContext(context);
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
     * @param contextToRemove
     *            the context to remove
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
     * Gets the message handlers.
     * 
     * @param contexts
     *            the contexts
     * @return the message handlers
     */
    private List<IMessageHandlerTester> getMessageHandlers(List<UserContextEntity> contexts) {
        List<IMessageHandlerTester> result = new ArrayList<IMessageHandlerTester>();
        for (UserContextEntity context : contexts) {
            result.add(context.getMessageHandler());
        }
        return result;

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
        testId = Math.round(Math.random() * System.currentTimeMillis() * random.nextLong());
        LoginContactInfoMessageHandler contactInfoMessageHandler = new LoginContactInfoMessageHandler();
        for (int i = 0; i < getNbUserConnected(); i++) {
            contactInfoMessageHandler.reset();
            UserContextEntity context = getConnectedUser("User" + i + "Login" + testId, "User" + i + "Password" + testId);
            getLog().info("Wait for contact info");
            int cptWait = 0;
            while (i != contactInfoMessageHandler.getNbContactInfoReceived()) {
                Thread.sleep(1000);
                getLog().info(i + " => " + contactInfoMessageHandler.getNbContactInfoReceived());
                assertTrue("Too much time waiting to inform other users", ++cptWait < 15);
            }
            for (MessageNoticeContactInfo messageNoticeContactInfo : contactInfoMessageHandler.getMessageNoticeContactInfos()) {
                assertNotNull(messageNoticeContactInfo.getUser());
                assertEquals(context.getUser().getId(), messageNoticeContactInfo.getUser().getId());
            }
            contexts.add(context);
            context.setMessageHandler(contactInfoMessageHandler);
        }
        putNewSimpleMessageHandlerInAllContext();
    }

    /**
     * Private convers.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
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
