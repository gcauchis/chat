package com.gc.irc.common.protocol.notice;

import java.util.ArrayList;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification used to send the list of all the connected client.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNoticeContactsList extends IRCMessageNotice {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2553210158579009868L;

	/** The liste users. */
	private ArrayList<IRCUser> listeUsers;

	/**
	 * Instantiates a new iRC message notice contacts list.
	 */
	private IRCMessageNoticeContactsList() {
		super(IRCMessageNoticeType.CONTACTS_LIST);
	}

	/**
	 * Instantiates a new iRC message notice contacts list.
	 * 
	 * @param listeUsers
	 *            the liste users
	 */
	public IRCMessageNoticeContactsList(final ArrayList<IRCUser> listeUsers) {
		this();
		this.listeUsers = listeUsers;
	}

	/**
	 * Gets the liste users.
	 * 
	 * @return the liste users
	 */
	public ArrayList<IRCUser> getListeUsers() {
		return listeUsers;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
	    final String TAB = " ";
	
	    StringBuilder retValue = new StringBuilder();
	    
	    retValue.append("IRCMessageNoticeContactsList ( ")
	        .append(super.toString()).append(TAB)
	        .append("listeUsers = ").append(this.listeUsers).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
