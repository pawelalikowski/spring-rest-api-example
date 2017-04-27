package com.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailService {
    private MailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    public MailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMail(SimpleMailMessage message) {

        try {
            this.mailSender.send(message);
            logger.info("Message sent: to:" + message.getTo() + ", subject:" + message.getSubject());
        }
        catch (MailException ex) {
            logger.error(ex.getMessage());
        }
    }
}
