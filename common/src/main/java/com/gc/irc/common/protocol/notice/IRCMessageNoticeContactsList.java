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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageNoticeContactsList [listeUsers=")
				.append(listeUsers).append("]");
		return builder.toString();
	}
}
