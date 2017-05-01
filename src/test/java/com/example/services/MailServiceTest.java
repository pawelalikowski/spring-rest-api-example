package com.example.services;

import com.example.models.Mail;
import freemarker.template.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class MailServiceTest {
    @MockBean private JavaMailSender mailSender;
    @MockBean private Configuration fmConfig;
    private MailService mailService;

    private Mail message;

    @Before
    public void setUp() throws Exception {
        message = new Mail();
        message.setTo("user@example.com");
        message.setText("test message");
        message.setSubject("test subject");
        this.mailService = new MailService(mailSender, fmConfig);
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
