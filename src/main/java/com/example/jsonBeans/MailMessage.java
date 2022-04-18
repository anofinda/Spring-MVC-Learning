package com.example.jsonBeans;

import com.example.entities.User;

/**
 * @author dongyudeng
 */
public class MailMessage {
    public enum Type{
        REGISTRATION,LOGIN;
    }
    public Type type;
    public String username,email;
    public static MailMessage registration(User user){
        MailMessage message=new MailMessage();
        message.type=Type.REGISTRATION;
        message.username=user.getUsername();
        message.email=user.getEmail();
        return message;
    }
    public static MailMessage login(User user){
        MailMessage message=new MailMessage();
        message.type=Type.LOGIN;
        message.username=user.getUsername();
        message.email=user.getEmail();
        return message;
    }
}
