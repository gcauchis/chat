package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification used to inform the success or the failed of the client
 * registration.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNoticeRegister extends IRCMessageNotice {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5439320167316655514L;

	/** The user. */
	private IRCUser user;

	/**
	 * Instantiates a new iRC message notice register.
	 */
	private IRCMessageNoticeRegister() {
		super(IRCMessageNoticeType.REGISTER);
	}

	/**
	 * Instantiates a new iRC message notice register.
	 * 
	 * @param user
	 *            the user
	 */
	public IRCMessageNoticeRegister(final IRCUser user) {
		this();
		this.user = user;
	}

	/**
	 * Checks if is succed.
	 * 
	 * @return true, if is succed
	 */
	public boolean isSucced() {
		if (user == null) {
			return false;
		}
		return true;
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
	    
	    retValue.append("IRCMessageNoticeRegister ( ")
	        .append(super.toString()).append(TAB)
	        .append("user = ").append(this.user).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
