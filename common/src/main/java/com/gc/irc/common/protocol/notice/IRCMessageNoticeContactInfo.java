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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageNoticeContactInfo [user=").append(user)
				.append("]");
		return builder.toString();
	}

}
