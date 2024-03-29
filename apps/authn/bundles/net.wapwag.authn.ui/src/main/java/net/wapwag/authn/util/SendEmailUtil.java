package net.wapwag.authn.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送邮件工具类
 * @author gongll
 *
 */
public class SendEmailUtil {

	private static EmailConfInfo confInfo;
	private static final Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);
	
	/**
	 * 发送重置密码短信
	 * @param resetKey
	 * @param sendEmail
	 * @param hostUrl
	 * @return
	 */
	public static boolean sendEmail(String resetKey, String sendEmail,
			String hostUrl) {
		logger.debug("Enter SendEmailUtil sendEmail()");
		if (null == confInfo || StringUtil.isEmp(confInfo.body)) {
			if (!initConfig()) {
				logger.error("SendEmailUtil Init email config errer");
				confInfo = null;
				return false;
			}
		}
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
		
		Thread.currentThread().setContextClassLoader(javax.mail.Message.class.getClassLoader());
		try {
			Session session = createSession();
			
			//nginx no set header message, so get host from properties.
			hostUrl = PropertiesUtil.RESET_HOST.value();
			System.out.println("hostUrl :" + hostUrl);
			MimeMessage message = createMessage(session, resetKey, sendEmail,
					hostUrl);

			Transport transport = session.getTransport();
			transport.connect(confInfo.server, confInfo.from, confInfo.pass);

			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}
		
		logger.debug("Exit SendEmailUtil sendEmail()");
		return true;
	}

	private static Session createSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", confInfo.protocol);
		props.setProperty("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		return session;

	}

	private static MimeMessage createMessage(Session session, String resetKey,
			String sendEmail, String hostUrl) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(confInfo.from));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(sendEmail));
		message.setSubject(confInfo.head);

		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart bodyPart = new MimeBodyPart();
		String sendMsg = confInfo.body;
		sendMsg = sendMsg.replace("RESET_PASSWD_URL", resetKey);
		sendMsg = sendMsg.replace("HOST_URL", hostUrl);
		bodyPart.setContent(sendMsg,"text/html;charset=utf-8");
		multipart.addBodyPart(bodyPart);

		message.setContent(multipart);
		message.saveChanges();
		return message;
	}
	
	private static boolean initConfig(){
		confInfo = new EmailConfInfo(); 
		try {
			StringBuffer emailMsg = new StringBuffer("");
			BufferedReader br = 
					new BufferedReader(new InputStreamReader(new FileInputStream(PropertiesUtil.AUTHN_EMAIl_TXT),"UTF-8"));  
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.indexOf("title") < 0) {
					emailMsg.append(str).append("<br/>");
				}else {
					confInfo.head = str.substring(7,str.length()-8);
				}
			}
			confInfo.body = emailMsg.toString();
			System.out.println("confInfo.body : " + confInfo.body);
			br.close();
			
			confInfo.from = PropertiesUtil.EMAIL_USER.value();
			confInfo.protocol = PropertiesUtil.EMAIL_PROTOCOL.value();
			confInfo.server = PropertiesUtil.EMAIL_SERVER.value();
			confInfo.pass = PropertiesUtil.EMAIL_PWD.value();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static class EmailConfInfo{
		String protocol;
		String from;
		String pass;
		String server;
		String head;
		String body;
	}
}