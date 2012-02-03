package com.gc.irc.common.protocol.command;

import com.gc.irc.common.entity.IRCUser;

/**
 * The Class IRCMessageCommandChangeNickname.
 */
public class IRCMessageCommandChangeNickname extends IRCMessageCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4197111507731979380L;
	/** The nickname. */
	private String nickname;

	/**
	 * Instantiates a new iRC message command change nickname.
	 * 
	 * @param user
	 *            the user
	 */
	public IRCMessageCommandChangeNickname(final IRCUser user) {
		super(user.getId(), IRCMessageCommandType.CHANGE_NICKNAME);
		nickname = user.getNickName();
	}

	/**
	 * Gets the new nickname.
	 * 
	 * @return the new nickname
	 */
	public String getNewNickname() {
		return nickname;
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
	    
	    retValue.append("IRCMessageCommandChangeNickname ( ")
	        .append(super.toString()).append(TAB)
	        .append("nickname = ").append(this.nickname).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
