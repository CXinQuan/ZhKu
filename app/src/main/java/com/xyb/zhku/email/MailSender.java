package com.xyb.zhku.email;

/**
 * Created by 陈鑫权  on 2018/12/8.
 */

import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
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
import javax.mail.internet.MimeUtility;

/**
 * 发送器
 */
public class MailSender {
    /**
     * 以文本格式发送邮件
     * 将自己封装的 MailInfo 消息，拆开 为真正的 Message 消息，才能实现发送
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(final MailInfo mailInfo) {

        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
//    Session sendMailSession = Session.getInstance(pro, new Authenticator() {
//      @Override
//      protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(mailInfo.getUserName(),mailInfo.getPassword());
//      }
//    });
        try {
            // 根据session创建一个邮件消息
            final Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress(), "校园信息交互平台");// 发送者的邮箱地址 ，显示发送者的用户名，默认邮箱账号，如：CXQRight
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date()); // 当前时间

            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);

            Transport.send(mailMessage);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(mailMessage);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
            // 发送邮件
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress(), "校园信息交互平台");// 发送者的邮箱地址 ，显示发送者的用户名，默认邮箱账号，如：CXQRight
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();  //多部分 类
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 发送带附件的邮件
     *
     * @param info
     * @return
     */
    public boolean sendFileMail(MailInfo info, File file) {


        Message attachmentMail = createAttachmentMail(info, file);

        // MailInfo myInfo = info;
        //  myInfo.setToAddress("CXQRight@163.com");
        //  Message myAttachmentMail = createAttachmentMail(myInfo, file);
        try {
            //先发一份给自己
            // Transport.send(myAttachmentMail);
            Transport.send(attachmentMail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            if (e == null) {
                Log.d("错误信息为空", "错误信息为空");
            }
            Log.d("错误信息", e.toString() + "错误信息");
            return false;
        }
    }

    /**
     * 创建带有附件的邮件
     *
     * @return
     */
    private Message createAttachmentMail(final MailInfo info, File file) {
        //创建邮件
        MimeMessage message = null;
        Properties pro = info.getProperties();
        try {
            Session sendMailSession = Session.getInstance(pro, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(info.getUserName(), info.getPassword());
                }
            });
            message = new MimeMessage(sendMailSession);
            // message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
            // 设置邮件的基本信息
            //创建邮件发送者地址
            Address from = new InternetAddress(info.getFromAddress(), "校园信息交互平台");// 发送者的邮箱地址 ，显示发送者的用户名，默认邮箱账号，如：CXQRight
            //设置邮件消息的发送者
            message.setFrom(from);
            //创建邮件的接受者地址，并设置到邮件消息中
            Address to = new InternetAddress(info.getToAddress());
            //设置邮件消息的接受者, Message.RecipientType.TO 属性表示接收者的类型为TO
            message.setRecipient(Message.RecipientType.TO, to);
            //邮件标题
            message.setSubject(info.getSubject());
            // 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用CharSet=UTF-8指明字符编码
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(info.getContent(), "text/html;charset=UTF-8");
            // 创建容器描述数据关系
            MimeMultipart mp = new MimeMultipart();//通过 多部分类 将正文与附件添加进去，将多部分类进行发送即可达到发送附加附件的邮件
            mp.addBodyPart(text);
            // 创建邮件附件
            MimeBodyPart attach = new MimeBodyPart();

            FileDataSource ds = new FileDataSource(file);
            DataHandler dh = new DataHandler(ds);
            attach.setDataHandler(dh);
            attach.setFileName(MimeUtility.encodeText(dh.getName()));
            mp.addBodyPart(attach);
            mp.setSubType("mixed");
            message.setContent(mp);
            message.saveChanges();

        } catch (Exception e) {
            Log.e("TAG", "创建带附件的邮件失败");
            e.printStackTrace();
        }
        // 返回生成的邮件
        return message;
    }
}