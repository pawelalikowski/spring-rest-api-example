package com.example.services;

import com.example.models.Mail;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;


@Service
public class MailService {
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    final Configuration fmConfiguration;

    @Autowired
    public MailService(JavaMailSender mailSender, Configuration fmConfiguration) {
        this.mailSender = mailSender;
        this.fmConfiguration = fmConfiguration;
    }

    @Async
    public void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            mimeMessageHelper.setTo(mail.getTo());
            String html = getContentFromTemplate(mail.getTemplate(), mail.getModel());
            mimeMessageHelper.setText(html, true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplate(String template, Map < String, Object > model) {
        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate(template), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
