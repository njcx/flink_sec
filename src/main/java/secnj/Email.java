package secnj;

import com.sun.mail.util.MailSSLSocketFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Email {
    private String myEmailSMTPHost ;
    private  String myEmailAccount ;
    private  String myEmailPassword ;
    private  String myEmailPort ;


    public Email(){
        Config config = ConfigFactory.load();
        myEmailSMTPHost = config.getString("output.email.email_username");
        myEmailAccount = config.getString("output.email.email_pwd");
        myEmailPassword = config.getString("output.email.email_host");
        myEmailPort= config.getString("output.email.email_smtp_port");
    }
    public void sendEmail(String toEmailAddress, String emailTitle, String emailContent) throws Exception{

        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.port", myEmailPort);
        props.setProperty("mail.smtp.host", myEmailSMTPHost);
        props.setProperty("mail.transport.protocol", "smtp");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getInstance(props);
        Message msg = new MimeMessage(session);
        msg.setSubject(emailTitle);
        msg.setSentDate(new Date());
        msg.setContent("<span style='color:red;'>html邮件测试...</span>", "text/html;charset=UTF-8");
        msg.setFrom(new InternetAddress(myEmailAccount,"你好！", "UTF-8"));
        Transport transport = session.getTransport();
        transport.connect( myEmailSMTPHost, myEmailAccount, myEmailPassword);
        transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress) });
        transport.close();
    }
}
//
//
//public class Email {
//
////
////    private   String EmailAccount ;
////    private   String EmailPassword ;
////    private   String EmailSMTPHost ;
//
//
//    private final String PROTOCOL = "smtp";
//    private final String HOST = "smtp.sina.com";
//    private final String PORT = "25";
//    private final static String IS_AUTH = "true";
//    private final static String IS_ENABLED_DEBUG_MOD = "true";
//    private String from = "xyang0917@sina.com";
//
//    private String to = "xyang0917@163.com";
//    private Properties props = null;
//
//    static {
//        props = new Properties();
//        props.setProperty("mail.transport.protocol", PROTOCOL);
//        props.setProperty("mail.smtp.host", HOST);
//        props.setProperty("mail.smtp.port", PORT);
//        props.setProperty("mail.smtp.auth", IS_AUTH);
//        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
//    }
//
//    public static void sendHtmlEmail(String to) throws Exception {
//        Session session = Session.getInstance(props, new MyAuthenticator());
//        MimeMessage message = new MimeMessage(session);
//        message.setSubject("html邮件主题");
//        message.setFrom(new InternetAddress(from));
//        message.setSentDate(new Date());
//        message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
//        message.setContent("<span style='color:red;'>html邮件测试...</span>", "text/html;charset=utf-8");
//        message.saveChanges();
//        Transport.send(message);
//    }
//
//
//    static class MyAuthenticator extends Authenticator {
//
//        private String username = "xyang0917";
//
//        private String password = "123456abc";
//        public MyAuthenticator() {
//            super();
//        }
//
//        public MyAuthenticator(String username, String password) {
//            super();
//            this.username = username;
//            this.password = password;
//        }
//
//        protected PasswordAuthentication getPasswordAuthentication() {
//            return new PasswordAuthentication(username, password);
//        }
//    }
//}





//
//    public Email(){
//        Config config = ConfigFactory.load();
//        EmailAccount = config.getString("output.email.email_username");
//        EmailPassword = config.getString("output.email.email_pwd");
//        EmailSMTPHost = config.getString("output.email.email_host");
//    }
//
//    public void sendEmail(String text,String receiveEmail) throws Exception {
//        Properties props = new Properties();
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.smtp.host", EmailAccount);
//        props.setProperty("mail.smtp.auth", "true");
//        final String smtpPort = "994";
//        props.setProperty("mail.smtp.port", smtpPort);
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
//        Session session = Session.getDefaultInstance(props);
//        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
//        MimeMessage message = createMimeMessage(session, EmailAccount, receiveEmail,text);
//        Transport transport = session.getTransport();
//        transport.connect(EmailAccount, EmailPassword);
//        transport.sendMessage(message, message.getAllRecipients());
//        transport.close();
//    }
//
//
//    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String text) throws Exception {
//        MimeMessage message = new MimeMessage(session);
//        message.setFrom(new InternetAddress(sendMail, "*****", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,"", "UTF-8"));
//        message.setSubject("邮箱验证", "UTF-8");
//        message.setContent(text, "text/html;charset=UTF-8");
//        message.setSentDate(new Date());
//        message.saveChanges();
//        return message;
//    }
////    public static void main(String[] args) throws Exception {
////        sendEmail("这是内容，验证码45845","183****1265@163.com");
////    }
//}