package sendmail;

import java.io.FileOutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Properties prop=new Properties();
		prop.setProperty("mail.host", "smtp.qq.com");
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		Session session= Session.getInstance(prop);
		session.setDebug(true);
		Transport ts=session.getTransport();
		ts.connect("smtp.qq.com", "493384625", "18225851554");
		Message message =createMixMail(session);
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
		
	}
	
	public static MimeMessage createMixMail(Session session) throws Exception{
		MimeMessage message=new MimeMessage(session);//创建邮件
		//设置邮件的基本信息
		message.setFrom(new InternetAddress("493384625@qq.com"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress("2443455443@qq.com"));
		message.setSubject("带附件和带图片的邮件");
		
		//正文
		MimeBodyPart text=new MimeBodyPart();
		text.setContent("xxx这是男的xxx<br><img src='cid:aaa.jpg'>","text/html;charset=utf-8");
		//图片
		MimeBodyPart image=new MimeBodyPart();
		image.setDataHandler(new DataHandler(new FileDataSource("src\\3.jpg")));
		image.setContentID("aaa.jpg");
		//附件1
		MimeBodyPart attach=new MimeBodyPart();
		DataHandler dh=new DataHandler(new FileDataSource("src\\4.zip"));
		attach.setDataHandler(dh);
		attach.setFileName(dh.getName());
		//描述关系:正文和图片
		MimeMultipart mp1=new MimeMultipart();
		mp1.addBodyPart(text);
		mp1.addBodyPart(image);
		mp1.setSubType("related");
		//描述关系:附件和附件
		MimeMultipart mp2=new MimeMultipart();

		mp2.addBodyPart(attach);
		
		//代表正文的bodypart
		MimeBodyPart content=new MimeBodyPart();
		content.setContent(mp1);
		mp2.addBodyPart(content);
		mp2.setSubType("mixed");
		message.setContent(mp2);
		message.saveChanges();
		message.writeTo(new FileOutputStream("E:\\MixedMail.eml"));
		return message;
	}

}
