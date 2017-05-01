package com.example.factories;

import com.example.models.Mail;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        private String template;

        private Map<String, Object> model = new HashMap<>();

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

        public Mail build() {
            if (isNullOrEmpty(to)) throw new IllegalStateException("to must be set");
            if (isNullOrEmpty(subject)) throw new IllegalStateException("subject must be set");
            if (isNullOrEmpty(template)) throw new IllegalStateException("template must be set");
            if (model.isEmpty()) throw new IllegalStateException("model must be set");
            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setTo(to);
            mail.setReplyTo(replyTo);
            mail.setCc(cc);
            mail.setBcc(bcc);
            mail.setSentDate(sentDate);
            mail.setSubject(subject);
            mail.setTemplate(template);
            mail.setModel(model);
            return mail;
        }

        private boolean isNullOrEmpty(String s) {
            return s == null || "".equals(s);
        }

        public MailMessageBuilder template(String name) {
            this.template = name;
            return this;
        }

        public MailMessageBuilder addModel(String key, Object value) {
            this.model.put(key, value);
            return this;
        }
    }
}