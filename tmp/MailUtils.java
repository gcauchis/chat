package com.acp.acs.common.utils;


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
 * @author a.bachkira
 * The Class MailUtils.
 */
public final class MailUtils {

    /**
     * Instantiates a new mail utils.
     */
    private MailUtils() {
        super();
    }

    public static boolean sendMailSMTP(String SMTPserveur,String SMTPport,String SMTPauthentication,final String SMTPlogin,final String SMTPpass,
    		String SMTPsocketPort,String SMTPsocketFallBack,String SMTPenabledSSL,String emailFrom ,
    		String subject,String emailTo ,String messageText) {
        boolean result = false;
		try     
		{   
			Properties props = System.getProperties();
			
			props.put("mail.smtp.host", SMTPserveur);  
			props.put("mail.smtp.auth", SMTPauthentication); 
			props.put("mail.smtp.port", SMTPport);    
			props.put("mail.smtp.socketFactory.port",SMTPsocketPort);
			props.put("mail.smtp.socketFactory.fallback", SMTPsocketFallBack);
			props.put("mail.smtp.ssl.enable", SMTPenabledSSL);   
			  
			Session session = null;
			if(!SMTPauthentication.equals("true")){
				session=Session.getDefaultInstance(props);
			}else{
				session=Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTPlogin, SMTPpass);   
				}  });
			}
			MimeMessage message = new MimeMessage(session);
		
			message.setFrom(new InternetAddress(emailFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			message.setSubject(subject);
			message.setText(messageText);
			message.setSentDate(new Date());
			
			Transport.send(message);
		} 
		catch (AddressException e) {
			e.printStackTrace();
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		}
		return result;
		}

}
