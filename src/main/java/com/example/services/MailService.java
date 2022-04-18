package com.example.services;

import com.example.entities.User;
import com.example.jsonBeans.MailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

/**
 * @author dongyudeng
 */
@Component
public class MailService {
    @Value("${smtp.from}")
    private String from;

    @Autowired
    JavaMailSender mailSender;

    public void sendRegistrationMail(User user) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Registration");
            String html = String.format("<p>Hi, %s,</p><p>Sent at %s</p>", user.getUsername(), LocalDateTime.now());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void sendRegistrationMail(MailMessage mailMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(from);
            helper.setTo(mailMessage.email);
            helper.setSubject("Registration");
            String html = String.format("<p>Hi, %s,</p><p>Sent at %s</p>", mailMessage.username, LocalDateTime.now());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void sendLoginMail(User user) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Login");
            String html = String.format("<p>Hi, %s,</p><p>Sent at %s</p>", user.getUsername(), LocalDateTime.now());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void sendLoginMail(MailMessage mailMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(from);
            helper.setTo(mailMessage.email);
            helper.setSubject("Registration");
            String html = String.format("<p>Hi, %s,</p><p>Sent at %s</p>", mailMessage.username, LocalDateTime.now());
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void sendMail(User user, String subject, String html) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }
}
