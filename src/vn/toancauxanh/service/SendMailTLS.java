package vn.toancauxanh.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

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

import org.zkoss.util.resource.Labels;

public class SendMailTLS {

	public static void sendEmailGmail(String mailTo, String subject, String content, List<String> attachFiles) {
		final String username = Labels.getLabel("conf.mail.email");
		final String password = Labels.getLabel("conf.mail.password");
		Properties props = new Properties();
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.auth", Labels.getLabel("conf.mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Labels.getLabel("conf.mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", Labels.getLabel("conf.mail.smtp.host"));
		props.put("mail.smtp.port", Labels.getLabel("conf.mail.smtp.port"));

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			if (!mailTo.isEmpty()) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
				message.setSubject(subject);

				// creates message part
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(MessageFormat.format(content, subject),
						"text/html; charset=utf-8");

				// creates multi-part
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				// adds attachments
				if (attachFiles != null && attachFiles.size() > 0) {
					for (String filePath : attachFiles) {
						MimeBodyPart attachPart = new MimeBodyPart();

						try {
							attachPart.attachFile(filePath);
						} catch (IOException ex) {
							ex.printStackTrace();
						}

						multipart.addBodyPart(attachPart);
					}
				}

				// sets the multi-part as e-mail's content
				message.setContent(multipart);

				// sends the e-mail
				Transport.send(message);
			}
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
