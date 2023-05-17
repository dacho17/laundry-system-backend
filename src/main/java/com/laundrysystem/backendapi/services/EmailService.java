package com.laundrysystem.backendapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.EmailDto;
import com.laundrysystem.backendapi.exceptions.ApiRuntimeException;
import com.laundrysystem.backendapi.services.interfaces.IEmailService;

@Service
public class EmailService implements IEmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String EMAIL_SUBJECT = "Password Reset Request";
    private static final String EMAIL_CONTENT = "You have requested password reset for user %s.\n\nAccess the following link to reset your password:\n%s/auth/reset-password?passwordResetToken=%s\n\nThe link will be active for the next 30 minutes.";
    
    @Autowired
    private JavaMailSender javaMailSender;
 
    @Value("${spring.mail.username}")
    private String sender;

    @Value("${client.url}")
    private String clientUrl;
 
    public void sendSimpleMail(EmailDto email) throws ApiRuntimeException {
        try {
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();
 
            mailMessage.setFrom(sender);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getContent());
            mailMessage.setSubject(email.getSubject());
 
            javaMailSender.send(mailMessage);
        }
 
        catch (MailException e) {
            logger.error(String.format("An exception occurred while sending an email [email=%s] with cause=[%s]. - [exc=%s]", email.toString(), e.getCause().getMessage(), e.getStackTrace().toString()));
            throw new ApiRuntimeException(e.getCause().getMessage());
        }
    }

    public EmailDto generatePasswordResetEmail(String email, String passwordResetToken, String username) {
        String emailContent = String.format(EMAIL_CONTENT, username, clientUrl, passwordResetToken);
        return new EmailDto(email, EMAIL_SUBJECT, emailContent);
    }
}
