package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification use to send the information of an user.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNoticeContactInfo extends IRCMessageNotice {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -177593849550243088L;

	/** The user. */
	private IRCUser user;

	/**
	 * Instantiates a new iRC message notice contact info.
	 */
	private IRCMessageNoticeContactInfo() {
		super(IRCMessageNoticeType.CONTACT_INFO);
	}

	/**
	 * Instantiates a new iRC message notice contact info.
	 * 
	 * @param user
	 *            the user
	 */
	public IRCMessageNoticeContactInfo(final IRCUser user) {
		this();
		this.user = user;
		setFromId(user.getId());
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public IRCUser getUser() {
		return user;
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
	    
	    retValue.append("IRCMessageNoticeContactInfo ( ")
	        .append(super.toString()).append(TAB)
	        .append("user = ").append(this.user).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
