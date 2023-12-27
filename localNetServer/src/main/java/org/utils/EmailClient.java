package org.utils;

import org.hsgt.core.domain.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailClient {

    Email email;
    Boolean debug;
    Session session;

    public EmailClient(Email email) {
        this.email = email;
        this.debug = true;
        this.session = null;
    }

    public Session applyGmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        // props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email.getEmailAddress(), email.getPassword());
                    }
                });
        return session;
    }

    public void applySmtpSession() {
        Properties props = new Properties();
        // props.put("mail.store.protocol", email.getTransportType());
        props.put("mail.smtp.host", email.getServer());
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getEmailAddress(), email.getPassword());
            }
        });
        this.session = session;
    }

    public void send(InternetAddress[] targetEmailAddress) throws UnsupportedEncodingException, MessagingException {
        Session session = this.session;
        session.setDebug(this.debug);
        MimeMessage message = new MimeMessage(session);
        String fromName = String.format("%s <%s>", MimeUtility.encodeWord(this.email.getUsername()), this.email.getEmailAddress());
        message.setFrom(new InternetAddress(fromName));
        message.setRecipients(Message.RecipientType.TO, targetEmailAddress);
        message.setSubject(email.getSubject());
        // message.setContent(email.getBody(), "text/html;charset=UTF-8");
        message.setText(email.getBody());
        message.saveChanges();

        Transport.send(message);
    }

}
