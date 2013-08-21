package com.gc.irc.common.protocol.command;

import com.gc.irc.common.entity.UserStatus;

/**
 * The Class IRCMessageCommandChangeStatus.
 */
public class MessageCommandChangeStatus extends MessageCommand {

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
    public MessageCommandChangeStatus(final long userId, final UserStatus newStatus) {
        super(userId);
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageCommandChangeStatus [").append(super.toString()).append(", newStatus=").append(newStatus).append("]");
        return builder.toString();
    }

}
