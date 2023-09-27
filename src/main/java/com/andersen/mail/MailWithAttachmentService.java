package com.andersen.mail;

import com.andersen.exception.EmailException;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class MailWithAttachmentService {

    private final String from;
    private final String password;
    private final String host;
    private final String port;
    private final String to;

    public MailWithAttachmentService(String from, String password, String host, String port, String to){
        this.from = from;
        this.password = password;
        this.host = host;
        this.port = port;
        this.to = to;
    }

    public void sendMail(String subject, String messageBody, String filename) {
        Session session = getSession();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageBody);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File("C:\\Users\\User\\IdeaProjects\\time-tracker\\Report.pdf"));
//            attachmentPart.attachFile(getFile(filename));
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new EmailException("Can't send email");
        }
    }

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.port", this.port);
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    private File getFile(String filename) {
        try {
            URI uri = this.getClass()
                    .getClassLoader()
                    .getResource(filename)
                    .toURI();
            return new File(uri);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to find file from resources: " + filename);
        }
    }
}