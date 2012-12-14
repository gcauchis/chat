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

/**
 * The Class MailUtils.
 */
public final class MailUtils {

    /**
     * Instantiates a new mail utils.
     */
    private MailUtils() {
        super();
    }

    public static boolean sendMailSMTP(final String SMTPserveur, final String SMTPport, final String SMTPauthentication, final String SMTPlogin,
            final String SMTPpass, final String SMTPsocketPort, final String SMTPsocketFallBack, final String SMTPenabledSSL, final String emailFrom,
            final String subject, final String emailTo, final String messageText) {
        final boolean result = false;
        try {
            final Properties props = System.getProperties();

            props.put("mail.smtp.host", SMTPserveur);
            props.put("mail.smtp.auth", SMTPauthentication);
            props.put("mail.smtp.port", SMTPport);
            props.put("mail.smtp.socketFactory.port", SMTPsocketPort);
            props.put("mail.smtp.socketFactory.fallback", SMTPsocketFallBack);
            props.put("mail.smtp.ssl.enable", SMTPenabledSSL);

            Session session = null;
            if (!SMTPauthentication.equals("true")) {
                session = Session.getDefaultInstance(props);
            } else {
                session = Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SMTPlogin, SMTPpass);
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
            e.printStackTrace();
        } catch (final MessagingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
