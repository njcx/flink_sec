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
