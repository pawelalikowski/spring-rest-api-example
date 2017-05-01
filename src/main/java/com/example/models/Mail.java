package com.example.models;

import org.springframework.mail.SimpleMailMessage;

import java.util.List;
import java.util.Map;

public class Mail extends SimpleMailMessage {
    private Map< String, Object > model;
    private List< Object > attachments;
    private String contentType;
    private String template;

    public Mail() {
        contentType = "text/plain";
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Mail mail = (Mail) o;

        if (model != null ? !model.equals(mail.model) : mail.model != null) return false;
        if (attachments != null ? !attachments.equals(mail.attachments) : mail.attachments != null) return false;
        return contentType != null ? contentType.equals(mail.contentType) : mail.contentType == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        return result;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
