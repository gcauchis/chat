package com.gc.irc.server.handler.message.test.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;

/**
 * The Class AbstractIRCMessageHandlerTest.
 * 
 * @param <MSGH>
 *            the generic type
 * @param <MSG>
 *            the generic type
 */
// @SpringApplicationContext("classpath*:spring-application-config.xml")
public abstract class AbstractMessageHandlerTest<MSGH extends IServerMessageHandler, MSG extends Message> /* extends UnitilsJUnit4 */{

    /** The message handler. */
    private MSGH messageHandler;

    /**
     * Allow.
     */
    @Test
    public void allow() {
        assertTrue(getMessageHandler().isHandled(buildMessageInstance()));
    }

    /**
     * Builds the message instance.
     * 
     * @return the mSG
     */
    protected abstract MSG buildMessageInstance();

    /**
     * Gets the message handler.
     * 
     * @return the message handler
     */
    public MSGH getMessageHandler() {
        return messageHandler;
    }

    /**
     * Injection.
     */
    @Test
    public void injection() {
        assertNotNull(getMessageHandler());
    }

    /**
     * Sets the message handler.
     * 
     * @param messageHandler
     *            the new message handler
     */
    // @SpringBeanByType
    public void setMessageHandler(MSGH messageHandler) {
        this.messageHandler = messageHandler;
    }

}
