package secnj;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
public class Email {


    private   String EmailAccount ;
    private   String EmailPassword ;
    private   String EmailSMTPHost ;



    public Email(){
        Config config = ConfigFactory.load();
        EmailAccount = config.getString("output.email.email_username");
        EmailPassword = config.getString("output.email.email_pwd");
        EmailSMTPHost = config.getString("output.email.email_host");
    }

    public void sendEmail(String text,String receiveEmail) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", EmailAccount);
        props.setProperty("mail.smtp.auth", "true");
        final String smtpPort = "994";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
        MimeMessage message = createMimeMessage(session, EmailAccount, receiveEmail,text);
        Transport transport = session.getTransport();
        transport.connect(EmailAccount, EmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String text) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, "*****", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,"", "UTF-8"));
        message.setSubject("邮箱验证", "UTF-8");
        message.setContent(text, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
//    public static void main(String[] args) throws Exception {
//        sendEmail("这是内容，验证码45845","183****1265@163.com");
//    }
}