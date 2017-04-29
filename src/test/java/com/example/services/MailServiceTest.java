package com.example.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class MailServiceTest {
    @MockBean private MailSender mailSender;
    private MailService mailService;

    private SimpleMailMessage message;

    @Before
    public void setUp() throws Exception {
        message = new SimpleMailMessage();
        message.setTo("user@example.com");
        message.setText("test message");
        message.setSubject("test subject");
        this.mailService = new MailService(mailSender);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sendMail_should_call_send() throws Exception {
        this.mailService.sendMail(message);
        verify(mailSender).send(message);
    }
}
