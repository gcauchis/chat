package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification used to inform the success or the failed of the client login.
 * 
 * @author gcauchis
 * 
 */
public class MessageNoticeLogin extends MessageNotice {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3882302853245775759L;

    /** The user. */
    private IRCUser user;

    /**
     * Instantiates a new iRC message notice login.
     * 
     * @param user
     *            the user
     */
    public MessageNoticeLogin(final IRCUser user) {
        this.user = user;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageNoticeLogin [").append(super.toString()).append(", user=").append(user).append("]");
        return builder.toString();
    }

}
