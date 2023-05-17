package com.laundrysystem.backendapi.services.interfaces;

import com.laundrysystem.backendapi.dtos.EmailDto;
import com.laundrysystem.backendapi.exceptions.ApiRuntimeException;

public interface IEmailService {
    EmailDto generatePasswordResetEmail(String email, String passwordResetToken, String username);
    void sendSimpleMail(EmailDto email) throws ApiRuntimeException;
}
