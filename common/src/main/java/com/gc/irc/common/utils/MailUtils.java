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
        } catch (final AddressException e) {
            LOGGER.warn("Fail to send email", e);
        } catch (final MessagingException e) {
            LOGGER.warn("Fail to send email", e);
        }
        return result;
    }

}
