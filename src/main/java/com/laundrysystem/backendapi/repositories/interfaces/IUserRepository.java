package com.laundrysystem.backendapi.repositories.interfaces;

import java.sql.Timestamp;
import java.util.List;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.UserRole;

public interface IUserRepository {
	User findById(int id);
	User findByUsername(String username);
	User findByEmail(String email);
	void save(User user);
	User update(User user);
	User setUserEmailAndMobileNumber(User user, String email, String dialCode, String mobileNumber);
	User setUserJwt(int userId, String jwt);
	
	List<User> getResidenceUsersOfType(int id, UserRole userRole);
	
	User generateResetPasswordData(User user, String passwordResetToken, Timestamp passwordResetValidUntil);
	User resetPasswordForUser(User user, String encrPass);
}
