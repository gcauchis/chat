package com.gc.irc.common.protocol.notice;

import java.util.List;

import com.gc.irc.common.entity.IRCUser;

/**
 * Notification used to send the list of all the connected client.
 * 
 * @author gcauchis
 * 
 */
public class MessageNoticeContactsList extends MessageNotice {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2553210158579009868L;

    /** The liste users. */
    private List<IRCUser> listeUsers;

    /**
     * Instantiates a new iRC message notice contacts list.
     * 
     * @param listeUsers
     *            the liste users
     */
    public MessageNoticeContactsList(final List<IRCUser> listeUsers) {
        this.listeUsers = listeUsers;
    }

    /**
     * Gets the liste users.
     * 
     * @return the liste users
     */
    public List<IRCUser> getListeUsers() {
        return listeUsers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageNoticeContactsList [").append(super.toString()).append(", listeUsers=").append(listeUsers).append("]");
        return builder.toString();
    }
}
