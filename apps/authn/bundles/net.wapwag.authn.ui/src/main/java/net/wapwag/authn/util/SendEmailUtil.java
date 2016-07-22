package net.wapwag.authn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmailUtil {

	private static final String protocol = "smtp"; 
	private static final String from = "gongll_wpg@sina.com";
	private static final String pass = "wpg123@";
	private static final String server = "smtp.sina.com";
	private static final String to = "1422655443@qq.com";

	private static final String subject = "[WPG] Please reset your password";
	public static void main(String[] args) throws Exception {

		sendEmail("213654", to,"http://localhost:8181/authn/");
	}

	public static boolean sendEmail(String resetKey,String sendEmail,String hostUrl) {
		try {
			Session session = createSession();
			MimeMessage message = createMessage(session,resetKey,sendEmail,hostUrl);

			System.out.println("正在发送邮件...");

			Transport transport = session.getTransport();
			transport.connect(server, from, pass);

			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			transport.close();

			System.out.println("发送成功!!!");
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private static Session createSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(props);
		return session;

	}

	private static MimeMessage createMessage(Session session , String resetKey,String sendEmail,String hostUrl) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(sendEmail));
		message.setSubject(subject);

		MimeMultipart multipart = new MimeMultipart("related");
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(createEmailBody(resetKey,hostUrl), "text/html;charset=gb2312");
		multipart.addBodyPart(bodyPart);

		message.setContent(multipart);
		message.saveChanges();
		return message;
	}

	private static String createEmailBody(String resetKey,String hostUrl) {
		try {
			
			File file = new File("aa.html");
			if (!file.exists()) {
				file.createNewFile();
				System.out.println(file.getAbsolutePath());
			}
			StringBuffer sb = new StringBuffer("");
			FileReader reader = new FileReader("conf"+File.separator+"email.html");
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
			reader.close();
			String sendMsg = sb.toString().replace("RESET_PASSWD_URL", resetKey);
			sendMsg = sendMsg.replace("HOST_URL", hostUrl);
			System.out.println(sendMsg);
			return sendMsg;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}