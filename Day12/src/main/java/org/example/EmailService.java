package org.example;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final Session session;

    public EmailService() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        String senderEmail = "23dtsa03@kristujayanti.com"; // 🔁 replace
        String password = "ciiu ozuc mbss jrcf";        // 🔁 replace (use App Password, not Gmail password)

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });
    }

    public void sendEmailAsync(String to, String subject, String content) {
        new Thread(() -> {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("23dtsa03@kristujayanti.com")); // 🔁 same as above
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(content);

                Transport.send(message);
                System.out.println("✅ Email sent to " + to);
            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println("❌ Failed to send email to " + to);
            }
        }).start();
    }
}
