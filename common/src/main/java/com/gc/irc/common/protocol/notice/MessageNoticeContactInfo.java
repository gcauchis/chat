package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.entity.User;

/**
 * Notification use to send the information of an user.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageNoticeContactInfo extends MessageNotice {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -177593849550243088L;

    /** The user. */
    private User user;

    /**
     * Instantiates a new iRC message notice contact info.
     *
     * @param user
     *            the user
     */
    public MessageNoticeContactInfo(final User user) {
        super();
        this.user = user;
        setFromId(user.getId());
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
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
        builder.append("IRCMessageNoticeContactInfo [").append(super.toString()).append(", user=").append(user).append("]");
        return builder.toString();
    }

}
