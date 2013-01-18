package com.gc.irc.server.thread.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

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
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.jms.api.IJMSProducer;
import com.gc.irc.server.jms.utils.JMSConnectionUtils;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.api.IUserPictureService;
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

    /**
     * Gets the nb thread.
     * 
     * @return the nb thread
     */
    private static synchronized int getNbThread() {
        nbThread++;
        return nbThread;
    }

    /** The authentication service. */
    private IAuthenticationService authenticationService;

    /** The id. */
    private final int id = getNbThread();

    /** The jms producer. */
    private IJMSProducer jmsProducer;

    /** The message consumer. */
    private MessageConsumer messageConsumer;

    /** The num passage max. */
    private int numPassageMax = 10;

    /** The user picture service. */
    private IUserPictureService userPictureService;

    /** The parent. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Builds the JMS message consumer.
     */
    private void buildMessageConsumer() {
        try {
            getLog().debug(id + " Create JMS Consumer");
            messageConsumer = JMSConnectionUtils.createConsumer();
        } catch (final JMSException e) {
            getLog().error(id + " Fail to create JMS Consumer : ", e);
        }
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
        final List<IGestionClientBean> clientConnecter = usersConnectionsManagement.getClientConnected();
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

        for (final IRCUser u : usersConnectionsManagement.getAllUsers()) {
            result += u.getId() + " : " + u.getNickName() + " | ";
        }

        return result;
    }

    /**
     * Handle Message.
     * 
     * @param message
     *            Message received.
     */
    private void handleObjectMessage(final ObjectMessage message) {
        getLog().debug(id + " Handle received Message.");
        if (message == null) {
            getLog().debug("Null message to handle");
            return;
        }

        /**
         * Update of the number of messages.
         */
        synchronized (nbMessage) {
            nbMessage++;
        }

        /**
         * Extract Message
         */
        IRCMessage messageObj = null;
        try {
            getLog().debug(id + " Extract Message receive in JMS");
            messageObj = (IRCMessage) message.getObject();
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to extract Message receive in JMS : " + e.getMessage());
        }
        if (messageObj == null) {
            getLog().debug("Null messageObj to handle");
            return;
        }

        getLog().debug(id + " Message's type : " + messageObj.getType());
        switch (messageObj.getType()) {
        case CHATMESSAGE:
            final IRCMessageChat messageObjChat = (IRCMessageChat) messageObj;
            getLog().debug(id + " Type : " + messageObjChat.getChatMessageType());
            switch (messageObjChat.getChatMessageType()) {
            case GLOBAL:
                if (authenticationService.getUser(messageObjChat.getFromId()) != null) {
                    getLog().debug(id + " Global message form " + authenticationService.getUser(messageObjChat.getFromId()).getNickname());
                    sendObjetMessageIRCToAll(messageObjChat);
                } else {
                    getLog().warn(id + " inexisting source id");
                }
                break;

            case PRIVATE:
                final IRCMessageChatPrivate messageChatPriv = (IRCMessageChatPrivate) messageObjChat;
                if (authenticationService.getUser(messageChatPriv.getFromId()) != null) {
                    if (authenticationService.getUser(messageChatPriv.getToId()) != null) {
                        if (getLog().isDebugEnabled()) {
                            getLog().debug(
                                    id + " Private Message from " + authenticationService.getUser(messageChatPriv.getFromId()).getNickname() + " to "
                                            + authenticationService.getUser(messageChatPriv.getToId()).getNickname());
                        }
                        final IGestionClientBean clientCible = usersConnectionsManagement.getGestionClientBeanOfUser(messageChatPriv.getToId());
                        if (clientCible != null) {
                            clientCible.sendMessageObjetInSocket(messageChatPriv);
                        }
                    } else {
                        getLog().warn(id + " inexisting destination id");
                        getLog().debug(id + " Check if retry later");
                        final int numPassage = messageChatPriv.numPassage();
                        if (numPassage < numPassageMax) {
                            getLog().debug(id + " Send again the private message in JMS. Passage number " + numPassage);
                            jmsProducer.postInJMS(messageChatPriv);
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
                    if (authenticationService.getUser(messageChNick.getFromId()) != null) {
                        authenticationService.changeNickUser(messageChNick.getFromId(), messageChNick.getNewNickname());
                        sendObjetMessageIRCToAll(new IRCMessageNoticeContactInfo(authenticationService.getUser(messageChNick.getFromId()).getUser()));
                    } else {
                        getLog().warn(id + " this user didn't exist.");
                    }
                }
                break;
            case CHANGE_STATUS:
                final IRCMessageCommandChangeStatus messageChStatus = (IRCMessageCommandChangeStatus) messageObjCmd;
                final IRCUser user = authenticationService.getUser(messageChStatus.getFromId()).getUser();
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

                userPictureService.newPicture(messagePictur.getFromId(), messagePictur);

                final UserInformations userInfo = authenticationService.getUser(messagePictur.getFromId());
                if (userInfo != null) {
                    userInfo.setHasPicture(true);
                    authenticationService.saveModification();
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

    /**
     * Initialize Thread.
     * 
     * Listening JMS Queue
     */
    private void init() {

        buildMessageConsumer();
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
        final IGestionClientBean thClient = usersConnectionsManagement.getGestionClientBeanOfUser(userID);
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
            waitAndHandleJMSMessage();
        }
    }

    /**
     * Send a message to all connected Client.
     * 
     * @param message
     *            Message to Send
     */
    private void sendObjetMessageIRCToAll(final IRCMessage message) {
        final List<IGestionClientBean> clientConnecter = usersConnectionsManagement.getClientConnected();

        if (authenticationService.getUser(message.getFromId()) != null) {
            getLog().isDebugEnabled();
            getLog().debug(id + " Send a message to all connected client from " + authenticationService.getUser(message.getFromId()).getNickname());
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
     * Sets the authentication service.
     * 
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(final IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the jms producer.
     * 
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(final IJMSProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    /**
     * Sets the num passage max.
     * 
     * @param numPassageMax
     *            the new num passage max
     */
    public void setNumPassageMax(final int numPassageMax) {
        this.numPassageMax = numPassageMax;
    }

    /**
     * @param userPictureService
     *            the userPictureService to set
     */
    public void setUserPictureService(final IUserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

    /**
     * Sets the users connections management.
     * 
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Wait and handle jms message.
     */
    private void waitAndHandleJMSMessage() {
        try {
            getLog().debug(id + " Wait for a message in JMS Queue");
            handleObjectMessage((ObjectMessage) messageConsumer.receive());
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to receive message in JMS Queue : ", e);
            try {
                messageConsumer.close();
            } catch (JMSException e1) {
                getLog().warn(id + " Fail to close messageConsumer Queue : ", e1);
            }
            buildMessageConsumer();
        }
    }
}
