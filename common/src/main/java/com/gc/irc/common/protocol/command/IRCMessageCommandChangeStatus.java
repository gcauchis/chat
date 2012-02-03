package com.gc.irc.common.protocol.command;

import com.gc.irc.common.entity.UserStatus;

/**
 * The Class IRCMessageCommandChangeStatus.
 */
public class IRCMessageCommandChangeStatus extends IRCMessageCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 764526201848393990L;

	/** The new status. */
	private UserStatus newStatus;

	/**
	 * Instantiates a new iRC message command change status.
	 * 
	 * @param userId
	 *            the user id
	 * @param newStatus
	 *            the new status
	 */
	public IRCMessageCommandChangeStatus(final int userId,
			final UserStatus newStatus) {
		super(userId, IRCMessageCommandType.CHANGE_STATUS);
		this.newStatus = newStatus;
	}

	/**
	 * Gets the new status.
	 * 
	 * @return the new status
	 */
	public UserStatus getNewStatus() {
		return newStatus;
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
	    
	    retValue.append("IRCMessageCommandChangeStatus ( ")
	        .append(super.toString()).append(TAB)
	        .append("newStatus = ").append(this.newStatus).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
