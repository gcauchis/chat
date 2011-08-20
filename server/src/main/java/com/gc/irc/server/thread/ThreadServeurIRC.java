package com.gc.irc.server.thread;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.log4j.Logger;

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
import com.gc.irc.server.ServeurIRC;
import com.gc.irc.server.auth.IRCServerAuthentification;
import com.gc.irc.server.auth.IRCUserInformations;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.jms.IRCJMSPoolProducer;
import com.gc.irc.server.jms.JMSConnection;
import com.gc.irc.server.persistance.IRCGestionPicture;

/**
 * Thread manager.
 * @author gcauchis
 *
 */
public class ThreadServeurIRC extends Thread implements IThreadServeurIRCMBean{
	private static int nbThread = 0;
	protected static int getNbThread() {
		nbThread++;
		return nbThread;
	}
	private int id = getNbThread();
	private static final Logger logger = Logger.getLogger(ThreadServeurIRC.class);
	private Session session = JMSConnection .getSession();
	private MessageConsumer messageConsumer = null;
	private ServeurIRC parent = null;
	private static int numPassageMax = Integer.parseInt(ServerConf.getConfProperty("nbMaxPassage", "10"), 10);
	
	private static Integer nbMessage = 0;
	
	/**
	 * Builder.
	 * @param parent Parent.
	 */
	public ThreadServeurIRC(ServeurIRC parent) {
		this.parent = parent;
	}
	
	/**
	 * Initialize Thread.
	 * 
	 * Listening JMS Queue
	 */
	private void init(){

		/**
		 * Create JMS Consumer
		 */
		try {
			logger.debug(id +" Create JMS Consumer");
			messageConsumer = session.createConsumer(JMSConnection.getQueue());
		} catch (JMSException e) {
			logger.fatal(id+" Fail to create JMS Consumer : "+e.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		logger.debug(id+" Start");
		init();
		
		
		while(true){
			Message message = null;
			try {
				/**
				 * Wait for a message in JMS Queue
				 */
				logger.debug(id+" Wait for a message in JMS Queue");
				message = messageConsumer.receive();
			} catch (JMSException e) {
				logger.warn(id+" Fail to receive message in JMS Queue : "+e.getMessage());
			}
			
			traitementObjecttMessage((ObjectMessage) message);
		}
	}
	
	/**
	 * Handle Message.
	 * @param message Message received.
	 */
	private void traitementObjecttMessage(ObjectMessage message){
		logger.debug(id+" Handle received Message.");
		IRCMessage messageObj = null;
		
		/**
		 * Update of the number of messages.
		 */
		synchronized(nbMessage) {
			nbMessage++;
		}
		
		
		/**
		 * Extract Message
		 */
		try {
			logger.debug(id+" Extract Message receive in JMS");
			messageObj = (IRCMessage) message.getObject();
		} catch (JMSException e) {
			logger.warn(id+" Fail to extract Message receive in JMS : "+e.getMessage());
		}

		logger.debug(id+" Message's type : "+ messageObj.getType());
		switch (messageObj.getType()) {
			case CHATMESSAGE:
				IRCMessageChat messageObjChat = (IRCMessageChat) messageObj;
				logger.debug(id+" Type : "+messageObjChat.getChatMessageType());
				switch (messageObjChat.getChatMessageType()) {
					case GLOBAL:
						if (IRCServerAuthentification.getInstance().getUser(messageObjChat.getFromId()) != null) {
							logger.debug(id+" Global message form "+IRCServerAuthentification.getInstance().getUser(messageObjChat.getFromId()).getNickname());
							sendObjetMessageIRCToAll(messageObjChat);
						} else {
							logger.warn(id+" inexisting source id");
						}
						break;
						
					case PRIVATE:
						IRCMessageChatPrivate messageChatPriv = (IRCMessageChatPrivate) messageObjChat;
						if (IRCServerAuthentification.getInstance().getUser(messageChatPriv.getFromId()) != null) {
							if (IRCServerAuthentification.getInstance().getUser(messageChatPriv.getToId()) != null) {
								logger.debug(id+" Private Message from "+IRCServerAuthentification.getInstance().getUser(messageChatPriv.getFromId()).getNickname()+" to "+IRCServerAuthentification.getInstance().getUser(messageChatPriv.getToId()).getNickname());
								ThreadGestionClientIRC clientCible = parent.getThreadOfUser(messageChatPriv.getToId());
								if (clientCible != null) {
									clientCible.envoyerMessageObjetSocket(messageChatPriv);
								}
							} else {
								logger.warn(id+" inexisting destination id");
								logger.debug(id+" Check if retry later");
								int numPassage = messageChatPriv.numPassage();
								if (numPassage < numPassageMax) {
									logger.debug(id+" Send again the private message in JMS. Passage number "+numPassage);
									IRCJMSPoolProducer.getInstance().postMessageObjectInJMS(messageChatPriv);
								} else {
									logger.debug(id+" Message passed to much time in the server (more than "+numPassageMax+"). Trash it !");
								}
							}
						} else {
							logger.warn(id+" inexisting source id");
						}
						break;
	
					default:
						break;
				}
				break;
			case COMMAND:
				IRCMessageCommand messageObjCmd = (IRCMessageCommand) messageObj;
				logger.debug(id+" Type : "+messageObjCmd.getCommandType());
				switch (messageObjCmd.getCommandType()) {
				case CHANGE_NICKNAME:
					IRCMessageCommandChangeNickname messageChNick = (IRCMessageCommandChangeNickname) messageObjCmd;
					{
						IRCServerAuthentification auth = IRCServerAuthentification.getInstance();
						if (auth.getUser(messageChNick.getFromId()) != null) {
							auth.changeNickUser(messageChNick.getFromId(), messageChNick.getNewNickname());
							sendObjetMessageIRCToAll(new IRCMessageNoticeContactInfo(auth.getUser(messageChNick.getFromId()).getUser()));
						} else {
							logger.warn(id+" this user didn't exist.");
						}
					}
					break;
				case CHANGE_STATUS:
					try {
						IRCMessageCommandChangeStatus messageChStatus = (IRCMessageCommandChangeStatus) messageObjCmd;
						IRCUser user = IRCServerAuthentification.getInstance().getUser(messageChStatus.getFromId()).getUser();
						if (user != null) {
							logger.debug(id+" "+user.getNickName()+" change statu to "+messageChStatus.getNewStatus());
							user.setUserStatus(messageChStatus.getNewStatus());
							sendObjetMessageIRCToAll(new IRCMessageNoticeContactInfo(user));
						}
					} catch (NullPointerException e) {
						logger.warn(id+" Null pointer exception.");
					}
					break;
				default:
					break;
				}			
				break;
			case NOTIFICATION:
				IRCMessageNotice messageObjNotice = (IRCMessageNotice) messageObj;
				logger.debug(id+" Type : "+messageObjNotice.getNoticeType());
				switch (messageObjNotice.getNoticeType()) {
				case CONTACT_INFO:
					IRCMessageNoticeContactInfo messageObjNoticeContactInfo = (IRCMessageNoticeContactInfo)messageObjNotice;
					IRCUser userChange = messageObjNoticeContactInfo.getUser();
					logger.debug(id+" User "+userChange.getNickName()+" change state to "+userChange.getUserStatus()+" has pictur : "+userChange.hasPictur());
					sendObjetMessageIRCToAll(messageObjNoticeContactInfo);
					break;

				default:
					break;
				}
				break;
			case ITEM:
				IRCMessageItemPicture messagePictur = (IRCMessageItemPicture) messageObj;
				{
					
					IRCGestionPicture.getInstance().newPicture(messagePictur.getFromId(), messagePictur);
					
					IRCServerAuthentification auth = IRCServerAuthentification.getInstance();
					IRCUserInformations userInfo = auth.getUser(messagePictur.getFromId());
					if (userInfo != null) {
						userInfo.setHasPicture(true);
					} else {
						logger.warn(id+" User null");
					}
					
					
					sendObjetMessageIRCToAll(messagePictur);
				}
				break;
	
			default:
				break;
		}
	}
	
	/**
	 * Send a message to all connected Client.
	 * @param message Message to Send
	 */
	private void sendObjetMessageIRCToAll(IRCMessage message) {
		List<ThreadGestionClientIRC> clientConnecter = parent.getClientConnecter();
		
		if (IRCServerAuthentification.getInstance().getUser(message.getFromId()) != null) {
			logger.debug(id +" Send a message to all connected client from "+IRCServerAuthentification.getInstance().getUser(message.getFromId()).getNickname());
			synchronized (clientConnecter) {
				for (ThreadGestionClientIRC client : clientConnecter) {
					if (message.getFromId() != client.getUser().getId()) {
						synchronized (client) {
							synchronized (client.getUser()) {
								client.envoyerMessageObjetSocket(message);
							}
						}
					}
				}
			}
		} else {
			logger.warn(id+" Inexisting source ID");
		}
	}
	
	/**
	 * Finalize the Thread
	 */
	public void finalizeClass() {
		logger.debug("Finalize the Thread");
		try {
			messageConsumer.close();
		} catch (JMSException e) {
			logger.warn(id+" Problem when close the messageConsumer : "+e.getMessage());
		}
		try {
			super.finalize();
		} catch (Throwable e) {
			logger.warn(id+" Fail to finalize class : "+e.getMessage());
		}
	}

	public int getNbMessages() {
		/**
		 * Get the number of messages (for JMX)
		 */
		return nbMessage;
	}

	public int getNbUser() {
		/**
		 * Get the number of connected client (for JMX)
		 */
		List<ThreadGestionClientIRC> clientConnecter = parent.getClientConnecter();
		return clientConnecter.size();
	}
	
	/**
	 * Return the list of online users (for JMX)
	 */
	public String getUserList() {
		String result = "";
		
		for(IRCUser u : parent.getAllUsers()){
			result += u.getId() + " : " + u.getNickName() + " | ";
		}
		
		return result;
	}
	
	public String kickUser(int userID) {
		/**
		 * Kick the user with the ID userID
		 */
		ThreadGestionClientIRC thClient = parent.getThreadOfUser(userID);
		if (thClient != null)
		{
			thClient.finalizeClass();
			return "Client successfully kicked";
		}
		else{
			return "Could not kick client";
		}
	}
}
