import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * Created by 鸭梨 on 2018/8/21.
 */
public class JavaMailTest {

    public static void main(String args[]) throws Exception {
       send("lilyzheng132@163.com","testing!!!!!!!!","测试是否成功");
     //  sendYouzanEmail("lilyzheng132@163.com","testing!!!!!!!!","测试是否在垃圾箱");

    }


    public static  void send(String address,String subject ,String content) throws MessagingException {
         final Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接  ---一般都使用
        properties.put("mail.debug", "true");//设置是否显示debug信息  true 会在控制台显示相关信息
        // 得到回话对象
         Session session = Session.getInstance(properties);
        // 获取邮件对象
         Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
         message.setFrom(new InternetAddress("1330354788@qq.com"));
        // 设置收件人地址
         message.setRecipients( MimeMessage.RecipientType.TO, new InternetAddress[] { new InternetAddress(address) });
        // 设置邮件标题
         message.setSubject(subject);
        // 设置邮件内容
         message.setText(content);

        URLName url = new URLName("smtp", "smtp.qq.com", 465, "",
                "1330354788@qq.com"  ,"zabzxcfklzdygfhb");
        Store store = session.getStore(url);
        store.connect();
        IMAPFolder folder = (IMAPFolder) store.getFolder("sent.BOX");
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);
        folder.appendMessages(new Message[]{message});


        // 得到邮差对象
         Transport transport = session.getTransport();
        // 连接自己的邮箱账户
         transport.connect("1330354788@qq.com", "zabzxcfklzdygfhb");//密码为刚才得到的授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
//        URLName(String protocol, String host, int port, String file, String username, String password)



    }

    private static void saveEmailToSentMailFolder(Message message) {

        String host = "pop.qq.com";
        String username = "1330354788@qq.com";
        String password = "生成的授权码";

        Properties p = new Properties();
        p.setProperty("mail.pop3.host", "pop.qq.com"); // 按需要更改
        p.setProperty("mail.pop3.port", "995");
        // SSL安全连接参数
        p.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.pop3.socketFactory.fallback", "true");
        p.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getDefaultInstance(p, null);
    //    Store store = session.getStore("pop3");
      //  store.connect(host, username, password);

        //Folder folder = store.getFolder("INBOX");
        //folder.open(Folder.READ_ONLY);



    }


    public static void sendYouzanEmail(String to, String subject, String content) throws Exception {
        final Properties properties = new Properties();
        //下面两段代码是设置ssl和端口，不设置发送不出去。
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        // 表示SMTP发送邮件，需要进行身份验证
        properties.setProperty("mail.transport.protocol", "smtp");// 设置传输协议
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.qq.com");//QQ邮箱的服务器 如果是企业邮箱或者其他邮箱得更换该服务器地址
        // 发件人的账号
        properties.put("mail.user", "1330354788@qq.com");
        // 访问SMTP服务时需要提供的密码
        properties.put("mail.password", "zabzxcfklzdygfhb");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = properties.getProperty("mail.user");
                String password = properties.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, authenticator);
        MimeMessage message = new MimeMessage(session);


        //设置图片
        MimeBodyPart image = new MimeBodyPart();
        image.setContent(content + "<img src='cid:a' modifysize=\"70%\" diffpixels=\"14px\" scalingmode=\"zoom\" style=\"width: 766px; height: 1361px;\">","text/html;charset=gb2312");
        MimeBodyPart img = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("D:\\ideaspace\\Fbusiness\\business_service_product\\src\\test\\java\\test.jpg"));//图片路径
        img.setDataHandler(dh);
        img.setContentID("a");
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(image);
        mm.addBodyPart(img);
        mm.setSubType("related");// 设置正文与图片之间的关系
        InternetAddress address = new InternetAddress();
        address.setAddress("1330354788@qq.com");
        address.setPersonal("【薪人薪事】");
        message.setFrom(address);
        message.setSubject(subject);
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSentDate(new Date());
        message.setContent(mm);
        Transport.send(message);


    }


}
