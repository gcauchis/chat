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
	 * @param user the user
	 */
	public IRCMessageCommandChangeNickname(IRCUser user) {
		super(user.getId(),IRCMessageCommandType.CHANGE_NICKNAME);
		this.nickname = user.getNickName();
	}
	
	/**
	 * Gets the new nickname.
	 *
	 * @return the new nickname
	 */
	public String getNewNickname(){
		return nickname;
	}

}
