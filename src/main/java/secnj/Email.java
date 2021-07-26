package secnj;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
public class Email {
    public static String myEmailAccount = "****";//邮箱账号
    public static String myEmailPassword = "****";//邮箱密码
    public static String myEmailSMTPHost = "smtp.ym.163.com";//发送邮箱服务器地址（这个地址是网易企业邮箱的地址）
    public static void sendEmail(String text,String receiveEmail) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", myEmailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");
        final String smtpPort = "994";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveEmail,text);
        Transport transport = session.getTransport();
        transport.connect(myEmailAccount, myEmailPassword);
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
    public static void main(String[] args) throws Exception {
        sendEmail("这是内容，验证码45845","183****1265@163.com");
    }
}