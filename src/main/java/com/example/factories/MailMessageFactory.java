package com.example.factories;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MailMessageFactory {
    public MailMessageBuilder create() {
        return new MailMessageBuilder();
    }

    public class MailMessageBuilder {
        private String from;

        private String replyTo;

        private String to;

        private String[] cc;

        private String[] bcc;

        private Date sentDate;

        private String subject;

        private String text;

        private MailMessageBuilder() {
        }


        public MailMessageBuilder to(String to) {
            this.to = to;
            return this;
        }

        public MailMessageBuilder from(String from) {
            this.from = from;
            return this;
        }

        public MailMessageBuilder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public MailMessageBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailMessageBuilder text(String text) {
            this.text = text;
            return this;
        }

        public MailMessageBuilder sentDate(Date sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        public MailMessageBuilder bcc(String[] bcc) {
            this.bcc = bcc;
            return this;
        }

        public MailMessageBuilder cc(String[] cc) {
            this.cc = cc;
            return this;
        }

        public SimpleMailMessage build() {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setReplyTo(replyTo);
            simpleMailMessage.setCc(cc);
            simpleMailMessage.setBcc(bcc);
            simpleMailMessage.setSentDate(sentDate);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            return simpleMailMessage;
        }
    }
}