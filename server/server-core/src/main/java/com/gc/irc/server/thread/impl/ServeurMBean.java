package com.gc.irc.server.thread.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.gc.irc.common.abs.AbstractRunnable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.common.protocol.command.IRCMessageCommand;
import com.gc.irc.common.protocol.command.IRCMessageCommandChangeNickname;
import com.gc.irc.common.protocol.command.IRCMessageCommandChangeStatus;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.common.protocol.notice.IRCMessageNotice;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;
import com.gc.irc.server.auth.IRCServerAuthentification;
import com.gc.irc.server.auth.IRCUserInformations;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.core.ServerCore;
import com.gc.irc.server.jms.IRCJMSPoolProducer;
import com.gc.irc.server.jms.JMSConnection;
import com.gc.irc.server.persistance.IRCGestionPicture;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.api.IServeurMBean;

/**
 * Thread manager.
 * 
 * @author gcauchis
 * 
 */
public class ServeurMBean extends AbstractRunnable implements IServeurMBean {

    /** The nb message. */
    private static Long nbMessage = 0L;

    /** The nb thread. */
    private static int nbThread = 0;

    /** The num passage max. */
    private static int numPassageMax = Integer.parseInt(ServerConf.getConfProperty(ServerConf.NB_MESSAGE_MAX_PASSAGE, "10"), 10);

    /**
     * Gets the nb thread.
     * 
     * @return the nb thread
     */
    protected static int getNbThread() {
        nbThread++;
        return nbThread;
    }

    /** The id. */
    private final int id = getNbThread();

    /** The message consumer. */
    private MessageConsumer messageConsumer = null;

    /** The parent. */
    private ServerCore parent = null;

    /** The session. */
    private final Session session = JMSConnection.getSession();

    /**
     * Builder.
     * 
     * @param parent
     *            Parent.
     */
    public ServeurMBean(final ServerCore parent) {
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#close()
     */
    @Override
    public void close() {
        getLog().debug("Finalize the Thread");
        try {
            messageConsumer.close();
        } catch (final JMSException e) {
            getLog().warn(id + " Problem when close the messageConsumer : " + e.getMessage());
        }
        try {
            super.finalize();
        } catch (final Throwable e) {
            getLog().warn(id + " Fail to finalize class : " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#getNbMessages()
     */
    @Override
    public long getNbMessages() {
        /**
         * Get the number of messages (for JMX)
         */
        return nbMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#getNbUser()
     */
    @Override
    public int getNbUser() {
        /**
         * Get the number of connected client (for JMX)
         */
        final List<IGestionClientBean> clientConnecter = parent.getClientConnected();
        return clientConnecter.size();
    }

    /**
     * Return the list of online users (for JMX).
     * 
     * @return the user list
     */
    @Override
    public String getUserList() {
        String result = "";

        for (final IRCUser u : parent.getAllUsers()) {
            result += u.getId() + " : " + u.getNickName() + " | ";
        }

        return result;
    }

    /**
     * Initialize Thread.
     * 
     * Listening JMS Queue
     */
    private void init() {

        /**
         * Create JMS Consumer
         */
        try {
            getLog().debug(id + " Create JMS Consumer");
            messageConsumer = session.createConsumer(JMSConnection.getQueue());
        } catch (final JMSException e) {
            getLog().error(id + " Fail to create JMS Consumer : " + e.getMessage());
            System.exit(-1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#kickUser(int)
     */
    @Override
    public String kickUser(final int userID) {
        /**
         * Kick the user with the ID userID
         */
        final IGestionClientBean thClient = parent.getGestionClientBeanOfUser(userID);
        if (thClient != null) {
            thClient.disconnectUser();
            return "Client successfully kicked";
        } else {
            return "Could not kick client";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        getLog().debug(id + " Start");
        init();

        while (true) {
            Message message = null;
            try {
                /**
                 * Wait for a message in JMS Queue
                 */
                getLog().debug(id + " Wait for a message in JMS Queue");
                message = messageConsumer.receive();
            } catch (final JMSException e) {
                getLog().warn(id + " Fail to receive message in JMS Queue : " + e.getMessage());
            }

            traitementObjectMessage((ObjectMessage) message);
        }
    }

    /**
     * Send a message to all connected Client.
     * 
     * @param message
     *            Message to Send
     */
    private void sendObjetMessageIRCToAll(final IRCMessage message) {
        final List<IGestionClientBean> clientConnecter = parent.getClientConnected();

        if (IRCServerAuthentification.getInstance().getUser(message.getFromId()) != null) {
            getLog().debug(
                    id + " Send a message to all connected client from " + IRCServerAuthentification.getInstance().getUser(message.getFromId()).getNickname());
            synchronized (clientConnecter) {
                for (final IGestionClientBean client : clientConnecter) {
                    if (message.getFromId() != client.getUser().getId()) {
                        synchronized (client) {
                            synchronized (client.getUser()) {
                                client.sendMessageObjetInSocket(message);
                            }
                        }
                    }
                }
            }
        } else {
            getLog().warn(id + " Inexisting source ID");
        }
    }

    /**
     * Handle Message.
     * 
     * @param message
     *            Message received.
     */
    private void traitementObjectMessage(final ObjectMessage message) {
        getLog().debug(id + " Handle received Message.");
        IRCMessage messageObj = null;

        /**
         * Update of the number of messages.
         */
        synchronized (nbMessage) {
            nbMessage++;
        }

        /**
         * Extract Message
         */
        try {
            getLog().debug(id + " Extract Message receive in JMS");
            messageObj = (IRCMessage) message.getObject();
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to extract Message receive in JMS : " + e.getMessage());
        }

        getLog().debug(id + " Message's type : " + messageObj.getType());
        switch (messageObj.getType()) {
        case CHATMESSAGE:
            final IRCMessageChat messageObjChat = (IRCMessageChat) messageObj;
            getLog().debug(id + " Type : " + messageObjChat.getChatMessageType());
            switch (messageObjChat.getChatMessageType()) {
            case GLOBAL:
                if (IRCServerAuthentification.getInstance().getUser(messageObjChat.getFromId()) != null) {
                    getLog().debug(id + " Global message form " + IRCServerAuthentification.getInstance().getUser(messageObjChat.getFromId()).getNickname());
                    sendObjetMessageIRCToAll(messageObjChat);
                } else {
                    getLog().warn(id + " inexisting source id");
                }
                break;

            case PRIVATE:
                final IRCMessageChatPrivate messageChatPriv = (IRCMessageChatPrivate) messageObjChat;
                if (IRCServerAuthentification.getInstance().getUser(messageChatPriv.getFromId()) != null) {
                    if (IRCServerAuthentification.getInstance().getUser(messageChatPriv.getToId()) != null) {
                        getLog().debug(
                                id + " Private Message from " + IRCServerAuthentification.getInstance().getUser(messageChatPriv.getFromId()).getNickname()
                                        + " to " + IRCServerAuthentification.getInstance().getUser(messageChatPriv.getToId()).getNickname());
                        final IGestionClientBean clientCible = parent.getGestionClientBeanOfUser(messageChatPriv.getToId());
                        if (clientCible != null) {
                            clientCible.sendMessageObjetInSocket(messageChatPriv);
                        }
                    } else {
                        getLog().warn(id + " inexisting destination id");
                        getLog().debug(id + " Check if retry later");
                        final int numPassage = messageChatPriv.numPassage();
                        if (numPassage < numPassageMax) {
                            getLog().debug(id + " Send again the private message in JMS. Passage number " + numPassage);
                            IRCJMSPoolProducer.getInstance().postMessageObjectInJMS(messageChatPriv);
                        } else {
                            getLog().debug(id + " Message passed to much time in the server (more than " + numPassageMax + "). Trash it !");
                        }
                    }
                } else {
                    getLog().warn(id + " inexisting source id");
                }
                break;

            default:
                break;
            }
            break;
        case COMMAND:
            final IRCMessageCommand messageObjCmd = (IRCMessageCommand) messageObj;
            getLog().debug(id + " Type : " + messageObjCmd.getCommandType());
            switch (messageObjCmd.getCommandType()) {
            case CHANGE_NICKNAME:
                final IRCMessageCommandChangeNickname messageChNick = (IRCMessageCommandChangeNickname) messageObjCmd;
                {
                    final IRCServerAuthentification auth = IRCServerAuthentification.getInstance();
                    if (auth.getUser(messageChNick.getFromId()) != null) {
                        auth.changeNickUser(messageChNick.getFromId(), messageChNick.getNewNickname());
                        sendObjetMessageIRCToAll(new IRCMessageNoticeContactInfo(auth.getUser(messageChNick.getFromId()).getUser()));
                    } else {
                        getLog().warn(id + " this user didn't exist.");
                    }
                }
                break;
            case CHANGE_STATUS:
                final IRCMessageCommandChangeStatus messageChStatus = (IRCMessageCommandChangeStatus) messageObjCmd;
                final IRCUser user = IRCServerAuthentification.getInstance().getUser(messageChStatus.getFromId()).getUser();
                if (user != null) {
                    getLog().debug(id + " " + user.getNickName() + " change status to " + messageChStatus.getNewStatus());
                    user.setUserStatus(messageChStatus.getNewStatus());
                    sendObjetMessageIRCToAll(new IRCMessageNoticeContactInfo(user));
                }
                break;
            default:
                break;
            }
            break;
        case NOTIFICATION:
            final IRCMessageNotice messageObjNotice = (IRCMessageNotice) messageObj;
            getLog().debug(id + " Type : " + messageObjNotice.getNoticeType());
            switch (messageObjNotice.getNoticeType()) {
            case CONTACT_INFO:
                final IRCMessageNoticeContactInfo messageObjNoticeContactInfo = (IRCMessageNoticeContactInfo) messageObjNotice;
                final IRCUser userChange = messageObjNoticeContactInfo.getUser();
                getLog()
                        .debug(
                                id + " User " + userChange.getNickName() + " change state to " + userChange.getUserStatus() + " has pictur : "
                                        + userChange.hasPictur());
                sendObjetMessageIRCToAll(messageObjNoticeContactInfo);
                break;

            default:
                break;
            }
            break;
        case ITEM:
            final IRCMessageItemPicture messagePictur = (IRCMessageItemPicture) messageObj;
            {

                IRCGestionPicture.getInstance().newPicture(messagePictur.getFromId(), messagePictur);

                final IRCServerAuthentification auth = IRCServerAuthentification.getInstance();
                final IRCUserInformations userInfo = auth.getUser(messagePictur.getFromId());
                if (userInfo != null) {
                    userInfo.setHasPicture(true);
                } else {
                    getLog().warn(id + " User null");
                }

                sendObjetMessageIRCToAll(messagePictur);
            }
            break;

        default:
            break;
        }
    }
}
