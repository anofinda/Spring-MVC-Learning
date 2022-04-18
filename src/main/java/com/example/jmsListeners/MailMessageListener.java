package com.example.jmsListeners;

import com.example.jsonBeans.MailMessage;
import com.example.services.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @author dongyudeng
 */
@Component
public class MailMessageListener {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MailService mailService;

    /**
     * onMailMessageReceived()方法可能被多线程并发执行，一定要保证线程安全。
     */
    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws JMSException, JsonProcessingException {
        logger.info("received message:" + message);
        if (message instanceof TextMessage) {
            String jsonText = ((TextMessage) message).getText();
            MailMessage mailMessage = objectMapper.readValue(jsonText, MailMessage.class);
            if (mailMessage.type.equals(MailMessage.Type.REGISTRATION)) {
                mailService.sendRegistrationMail(mailMessage);
            } else if (mailMessage.type.equals(MailMessage.Type.LOGIN)) {
                mailService.sendLoginMail(mailMessage);
            } else {
                logger.info("unknown mailMessage received.");
            }
        } else {
            logger.error("unable to process non-text message!");
        }
    }
}
