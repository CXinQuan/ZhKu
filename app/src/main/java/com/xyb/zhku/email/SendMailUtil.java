package com.xyb.zhku.email;

import android.support.annotation.NonNull;

import com.xyb.zhku.global.ZhKuThreadPool;

import java.io.File;

/**
 * Created by 陈鑫权  on 2018/12/8.
 */

public class SendMailUtil {
    //qq 邮箱  SMTP服务器（端口465或587）:smtp.qq.com    POP3服务器（端口995）:pop.qq.com
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "1611205417@qq.com"; //teprinciple@foxmail.com
    private static final String FROM_PSW = "orzvtuwcvzkxccfe";//lfrlpganzjrwbeci  此处的密码应该是 授权码 ，而不是登录密码


    // TODO: 正式开发，这里的配置需要将其放在服务器端，在需要的时候，进行联网获取，类似微信支付或者支付宝支付
    // TODO: 但是这里由于服务器是利用Bmob平台进行搭建的，所以出于安全的考虑，与其将配置放在三方，还不如放在自己的app中

    //163
//    private static final String HOST = "smtp.163.com";
//    private static final String PORT = "25"; // 经测试必须是25端口
//    private static final String FROM_ADD = "CXQRight@163.com"; //teprinciple@foxmail.com
//    private static final String FROM_PSW = "8964296143813we";//lfrlpganzjrwbeci  此处的密码应该是 授权码 ，而不是登录密码


    //1611205417@qq.com
    //163
    //  private static final String HOST = "smtp.163.com";
    //  private static final String PORT = "25"; //或者465 994
    //  private static final String FROM_ADD = "teprinciple@163.com";//teprinciple@163.com
    //  private static final String FROM_PSW = "teprinciple163";//teprinciple163

    //private static final String TO_ADD = "1611205417@qq.com";//2584770373@qq.com

    public static void send(final File file, String toAdd, String subject, String content) {
        final MailInfo mailInfo = creatMail(toAdd, subject, content);
        final MailSender sms = new MailSender();
        ZhKuThreadPool.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo, file);
            }
        });
    }

    public static void send(String toAdd, String subject, String content) {
        final MailInfo mailInfo = creatMail(toAdd, subject, content);
        final MailSender sms = new MailSender();
        ZhKuThreadPool.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        });

    }

    @NonNull
    private static MailInfo creatMail(String toAdd, String subject, String content) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去toAdd  TO_ADD
        mailInfo.setSubject(subject); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }
}