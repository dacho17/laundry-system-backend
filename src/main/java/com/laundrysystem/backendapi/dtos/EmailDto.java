package com.laundrysystem.backendapi.dtos;

public class EmailDto {
    private String recipient;
    private String subject;
    private String content;

    public EmailDto() {}

    public EmailDto(String recipient, String subject, String content) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EmailDto [recipient=" + recipient + ", subject=" + subject + ", content=" + content + "]";
    }
}
