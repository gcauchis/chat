package com.gc.irc.common.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.gc.irc.common.protocol.IRCMessage;

public class IOStreamUtils {

    /** The Constant logger. */
    private static final Logger LOGGER = Logger.getLogger(IRCMessage.class);

    /**
     * Send the message.
     * 
     * @param outObject
     *            Stream where is send the message
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void sendMessage(final ObjectOutputStream outObject, final IRCMessage message) throws IOException {
        LOGGER.debug("Send the Message : " + message);
        outObject.writeObject(message);
        outObject.flush();
        outObject.reset();
    }

    /**
     * Wait and receive a Message.
     * 
     * @param inObject
     *            Stream to listen.
     * @return Message Received message.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static IRCMessage receiveMessage(final ObjectInputStream inObject) throws ClassNotFoundException, IOException {

        LOGGER.debug("Wait for a message in the Stream.");
        final IRCMessage message = (IRCMessage) inObject.readObject();
        LOGGER.debug("Message receive : " + message);

        return message;
    }

}
