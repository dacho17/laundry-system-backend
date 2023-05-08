package com.laundrysystem.backendapi.repositories.interfaces;

import java.util.List;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.UserRole;

public interface IUserRepository {
	User findById(int id);
	User findByUsername(String username);
	void save(User user);
	User update(User user);
	User setUserEmailAndMobileNumber(User user, String email, String mobileNumber);
	User setUserJwt(int userId, String jwt);
	
	List<User> getResidenceUsersOfType(int id, UserRole userRole);
}
