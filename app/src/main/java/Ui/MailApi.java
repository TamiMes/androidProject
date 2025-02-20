package Ui;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MailApi {

    private final String senderEmail;
    private final String senderPassword;
    private final String recipientEmail;
    private final String emailSubject;
    private final String jsonMessage;

    public MailApi(String senderEmail, String senderPassword, String recipientEmail, String emailSubject, String jsonMessage) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.recipientEmail = recipientEmail;
        this.emailSubject = emailSubject;
        this.jsonMessage = jsonMessage;
    }

    public void sendEmail() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(emailSubject);

        // Set JSON message as content
        message.setText(jsonMessage);

        Transport.send(message);
    }
}
