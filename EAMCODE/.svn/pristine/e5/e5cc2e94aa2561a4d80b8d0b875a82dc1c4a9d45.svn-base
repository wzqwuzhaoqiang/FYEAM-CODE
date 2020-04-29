package com.fuyaogroup.eam.util;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * Created by Mcin on 2017/5/18.
 */
public class EmailUtil {
    
    private static final Logger logger = Logger.getLogger(EmailUtil.class);

    // 这是腾讯企业邮箱的  如果是其他邮箱 自行更换
    static String MAIL_TRANSPORT_PROTOCOL = "smtp"; //邮箱协议
    static String MAIL_SMTP_HOST = "mail.fuyaogroup.com"; //发件服务器地址
    static String MAIL_SMTP_PORT = "465"; // 端口
    static String MAIL_SMTP_AUTH = "true"; //使用smtp身份验证
    final static String USER_NAME = "noreply@fuyaogroup.com"; // 要登陆的企业邮箱账号
    final static String PASS_WORD = "Fuyao2018"; //要登陆的企业邮箱密码
    /**
     * 邮箱配置
     */
    public static Properties setTencentExEmail (){
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", MAIL_TRANSPORT_PROTOCOL);
        //服务器
        prop.setProperty("mail.smtp.host", MAIL_SMTP_HOST);
        //端口
        prop.setProperty("mail.smtp.port", MAIL_SMTP_PORT);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", MAIL_SMTP_AUTH);

        //开启安全协议 使用SSL，企业邮箱必需！
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        return prop;
    }
    
    static long startTime , endTime; // 用于计算发送的时间耗时

    /**
     * 发送email给邮件组
     * @param address
     * @param subject
     * @param content
     * @throws Exception
     */
    public  void sendEmail(List<String> address,String subject,String content) throws Exception {
        //获取Session对象
        Session session = Session
                .getDefaultInstance(
                		EmailUtil.setTencentExEmail(),
                        new Authenticator() {
            //此访求返回用户和密码的对象
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                PasswordAuthentication pa = new PasswordAuthentication(USER_NAME, PASS_WORD);
                return pa;
            }
        });
        //设置session的调试模式，发布时取消
//        session.setDebug(true);

          /*
          // 有循环的情况下，如果实现群发的功能  比如 收件人方可以显示到多少个收件用户的
          MimeMessage mimeMessage = new MimeMessage(session);
          mimeMessage.setFrom(new InternetAddress(userName,userName));*/

        for (int i = 0; i <address.size() ; i++) {

            // 有循环的情况下，实现单独发送的功能 收件人方只显示自己的邮箱
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(USER_NAME,USER_NAME));

            mimeMessage.addRecipient(Message
                    .RecipientType
                    .TO,
                    new InternetAddress(
                            address.get(i)
                    ));

            //设置主题
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            //设置内容
            mimeMessage.setText(content);
            mimeMessage.saveChanges();
            logger.info("***开始发送第 "+(i+1)+" 个邮件***");
            startTime = System.currentTimeMillis();
            try {
                //发送
                Transport.send(mimeMessage);
                endTime = System.currentTimeMillis();
                logger.info("第 "+(i+1)+" 个发送成功***耗时："
                        +(endTime - startTime)/1000+" 秒");
                logger.info("-------------------------------------------------------------");
            } catch (MessagingException e) {
                logger.error(e.getMessage());
                continue;
            }
        }
    }
}