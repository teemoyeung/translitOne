import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Source Code: https://www.youtube.com/watch?v=UMfjndwGwnM
//activation.jar
//javaee-api-7.0.jar
//javax.mail-1.4.4.jar
//mail.jar

public class GmailEmail {

	public static void main(String[] args) {

		final String username = "Translit321@gmail.com";
		final String password = "Translit";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("Translit321@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("Translit321@gmail.com"));
			message.setSubject("subjectInput.getText()");
			message.setText("Dear Me,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}