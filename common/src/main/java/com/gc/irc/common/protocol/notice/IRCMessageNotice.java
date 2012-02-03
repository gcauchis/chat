package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;

/**
 * Represent notification message.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNotice extends IRCMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7286654418377130362L;

	/** The notice type. */
	private IRCMessageNoticeType noticeType;

	/**
	 * Instantiates a new iRC message notice.
	 */
	public IRCMessageNotice() {
		super(0, IRCMessageType.NOTIFICATION);
	}

	/**
	 * Instantiates a new iRC message notice.
	 * 
	 * @param noticeType
	 *            the notice type
	 */
	public IRCMessageNotice(final IRCMessageNoticeType noticeType) {
		this();
		this.noticeType = noticeType;
	}

	/**
	 * Gets the notice type.
	 * 
	 * @return the notice type
	 */
	public IRCMessageNoticeType getNoticeType() {
		return noticeType;
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
	    
	    retValue.append("IRCMessageNotice ( ")
	        .append(super.toString()).append(TAB)
	        .append("noticeType = ").append(this.noticeType).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
