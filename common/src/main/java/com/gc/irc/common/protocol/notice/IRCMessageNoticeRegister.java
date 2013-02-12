package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification used to inform the success or the failed of the client
 * registration.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNoticeRegister extends IRCMessageNoticeLogin {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5439320167316655514L;

    /**
     * Instantiates a new iRC message notice register.
     * 
     * @param user
     *            the user
     */
    public IRCMessageNoticeRegister(final IRCUser user) {
        super(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageNoticeRegister [").append(super.toString()).append(", ]");
        return builder.toString();
    }
}
