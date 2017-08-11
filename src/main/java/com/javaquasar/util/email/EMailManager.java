package com.javaquasar.util.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
/**
 *
 * @author artur
 */
public class EMailManager {
       
    private Session session = null;
    private Map<String, String> headers = null;
    
    public EMailManager(Properties smtpProperties) {
        session = Session.getDefaultInstance(smtpProperties);
    }
    
    public EMailManager(Properties smtpProperties, String login, String password) {
        Authenticator authenticator = new SMTPAuthenticator(login, password);
        session = Session.getDefaultInstance(smtpProperties, authenticator);
    }
    
    public EMailManager(Properties smtpProperties, String login, String password, Map<String, String> headers) {
        Authenticator authenticator = new SMTPAuthenticator(login, password);
        session = Session.getDefaultInstance(smtpProperties, authenticator);
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    } 

    private MimeMessage getMimeMessage(String sender, String recipient, String subject) {
        MimeMessage msg = null;
        try {
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            
            if(headers != null) {
                for(Map.Entry<String, String> entry : headers.entrySet()) {
                     msg.addHeader(entry.getKey(), entry.getValue());
                }
            }
           
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return msg;
    }

    //recipientsList
    public void sendEMail(String sender, String recipient, String subject, String contentType, String messageBody) throws MessagingException, IOException {
        try {
            MimeMessage msg = getMimeMessage(sender, recipient, subject);
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(messageBody.getBytes(StandardCharsets.UTF_8), contentType)));
            Transport.send(msg);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void sendEMailWithMultipleAttachment(
            String sender,
            String recipient,
            String subject,
            String contentType,
            String messageBody,
            List<String> attachments) 
            throws MessagingException, IOException {
        try {
            MimeMessage msg = getMimeMessage(sender, recipient, subject);
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(messageBody.getBytes(StandardCharsets.UTF_8), contentType)));
            multipart.addBodyPart(messageBodyPart);

            for (String attachmentFileName : attachments) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentFileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(FileSystems.getDefault().getPath(attachmentFileName).getFileName().toString());
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);

            Transport.send(msg);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void sendEMailWithAttachment(
            String sender, 
            String recipient,
            String subject, 
            String contentType,
            String messageBody, 
            String attachmentFileName) throws MessagingException, IOException {
        sendEMailWithMultipleAttachment(sender, recipient, subject, contentType, messageBody, Arrays.asList(attachmentFileName));
    }

    public void sendEMailWithImageAttachment(
            String sender,
            String recipient,
            String subject,
            String contentType /*"text/html"*/,
            String messageBody,
            Map<String, String> mapInlineImages,
            String attachmentImage) throws MessagingException, IOException {
        try {
            MimeMessage msg = getMimeMessage(sender, recipient, subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(messageBody.getBytes(StandardCharsets.UTF_8), contentType)));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (mapInlineImages != null && mapInlineImages.size() > 0) {
                Set<String> setImageID = mapInlineImages.keySet();

                for (String contentId : setImageID) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setHeader("Content-ID", "<" + contentId + ">");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    String imageFilePath = mapInlineImages.get(contentId);
                    imagePart.attachFile(imageFilePath);
                    multipart.addBodyPart(imagePart);
                }
            }

            MimeBodyPart fileBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentImage);
            fileBodyPart.setDataHandler(new DataHandler(source));
            fileBodyPart.setFileName(FileSystems.getDefault().getPath(attachmentImage).getFileName().toString());
            multipart.addBodyPart(fileBodyPart);

            msg.setContent(multipart);
            Transport.send(msg);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static class SMTPAuthenticator extends Authenticator {

        private final String login;
        private final String password;
        
        public SMTPAuthenticator(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(login, password);
        }
    }
}
