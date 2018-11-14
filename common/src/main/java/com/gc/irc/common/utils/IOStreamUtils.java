package com.gc.irc.common.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;

import com.gc.irc.common.protocol.Message;

/**
 * <p>IOStreamUtils class.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
public final class IOStreamUtils {

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerUtils.getLogger(IOStreamUtils.class);

    /**
     * Wait and receive a Message.
     *
     * @param inObject
     *            Stream to listen.
     * @return Message Received message.
     * @throws java.lang.ClassNotFoundException
     *             the class not found exception
     * @throws java.io.IOException
     *             Signals that an I/O exception has occurred.
     */
    public static Message receiveMessage(final ObjectInputStream inObject) throws ClassNotFoundException, IOException {

        LOGGER.debug("Wait for a message in the Stream.");
        final Message message = (Message) inObject.readObject();
        LOGGER.debug("Message receive : " + message);

        return message;
    }

    /**
     * Send the message.
     *
     * @param outObject
     *            Stream where is send the message
     * @throws java.io.IOException
     *             Signals that an I/O exception has occurred.
     * @param message a {@link com.gc.irc.common.protocol.Message} object.
     */
    public static void sendMessage(final ObjectOutputStream outObject, final Message message) throws IOException {
        LOGGER.debug("Send the Message : " + message);
        outObject.writeObject(message);
        outObject.flush();
        outObject.reset();
        LOGGER.debug("Message sended");
    }

    /**
     * Instantiates a new iO stream utils.
     */
    private IOStreamUtils() {
        super();
    }

}
