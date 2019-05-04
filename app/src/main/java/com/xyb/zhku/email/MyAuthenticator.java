package com.xyb.zhku.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by 陈鑫权  on 2018/12/8.
 */

public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
