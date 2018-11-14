package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.User;

/**
 * Notification used to inform the success or the failed of the client
 * registration.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageNoticeRegister extends MessageNoticeLogin {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5439320167316655514L;

    /**
     * Instantiates a new iRC message notice register.
     *
     * @param user
     *            the user
     */
    public MessageNoticeRegister(final User user) {
        super(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageNoticeRegister [").append(super.toString()).append(", ]");
        return builder.toString();
    }
}
