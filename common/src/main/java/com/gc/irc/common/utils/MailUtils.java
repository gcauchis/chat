package com.gc.irc.common.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

/**
 * The Class MailUtils.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public final class MailUtils {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerUtils.getLogger(MailUtils.class);

    /**
     * Instantiates a new mail utils.
     */
    private MailUtils() {
        super();
    }

    /**
     * <p>sendMailSMTP.</p>
     *
     * @param smtpServeur a {@link java.lang.String} object.
     * @param smtpPort a {@link java.lang.String} object.
     * @param smtpAuthentication a {@link java.lang.String} object.
     * @param smtpLogin a {@link java.lang.String} object.
     * @param smtpPass a {@link java.lang.String} object.
     * @param smtpSocketPort a {@link java.lang.String} object.
     * @param smtpSocketFallBack a {@link java.lang.String} object.
     * @param smtpEnabledSSL a {@link java.lang.String} object.
     * @param emailFrom a {@link java.lang.String} object.
     * @param subject a {@link java.lang.String} object.
     * @param emailTo a {@link java.lang.String} object.
     * @param messageText a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean sendMailSMTP(final String smtpServeur, final String smtpPort, final String smtpAuthentication, final String smtpLogin,
            final String smtpPass, final String smtpSocketPort, final String smtpSocketFallBack, final String smtpEnabledSSL, final String emailFrom,
            final String subject, final String emailTo, final String messageText) {
        final boolean result = false;
        try {
            final Properties props = System.getProperties();

            props.put("mail.smtp.host", smtpServeur);
            props.put("mail.smtp.auth", smtpAuthentication);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.socketFactory.port", smtpSocketPort);
            props.put("mail.smtp.socketFactory.fallback", smtpSocketFallBack);
            props.put("mail.smtp.ssl.enable", smtpEnabledSSL);

            Session session = null;
            if (!smtpAuthentication.equals("true")) {
                session = Session.getDefaultInstance(props);
            } else {
                session = Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpLogin, smtpPass);
                    }
                });
            }
            final MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(emailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject(subject);
            message.setText(messageText);
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.warn("Fail to send email", e);
        }
        return result;
    }

}
