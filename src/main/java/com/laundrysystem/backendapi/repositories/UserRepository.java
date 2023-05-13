package com.laundrysystem.backendapi.repositories;

import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.entities.Tenancy;
import com.laundrysystem.backendapi.enums.UserRole;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class UserRepository implements IUserRepository {
	@Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
	
	public User findById(int id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> query = cb.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    query = query.select(root)
			.where(cb.and(
					cb.equal(root.get("id"), id)
			));
	    
	    try {
			return entityManager.createQuery(query).getSingleResult();			
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.warn(String.format("User with id=%d has not been found.", id));
			return null;
		}
	}

	public User setUserEmailAndMobileNumber(User user, String email, String dialCode, String mobileNumber) {
		logger.info(String.format("About to change the user's (userId=%1$d) email address and mobileNumber ["
				+ "{oldEmail=%2$s, newEmail=%3$s}, {oldDialCode=%4$s, newDialCode=%5$s} {oldMobileNumber=%6$s, newMobileNumber=%7$s}]",
				user.getId(), user.getEmail(), email, user.getCountryDialCode(), dialCode, user.getMobileNumber(), mobileNumber));
		user.setEmail(email);
		user.setCountryDialCode(dialCode);
		user.setMobileNumber(mobileNumber);
		user = update(user);

		logger.info(String.format("Successfully updated email and mobile number for the user with userId=%d.", user.getId()));
		return user;
	}
	
	public User setUserJwt(int userId, String jwt) {
		logger.info(String.format("Setting jwt=%s for user with id=%d", jwt, userId));
		User user = findById(userId);
		
		if (user == null) {
			logger.error("User with id=%d does not exist and the jwt can not be removed from them.", userId);
			return null;
		}
		
		user.setJwt(jwt);
		user = update(user);
		logger.info(String.format("Jwt=%s set for user with id=%d", jwt, user.getId()));
		
		return user;
	}
	
	public List<User> getResidenceUsersOfType(int residenceId, UserRole userRole) {
		logger.info(String.format("Fetching users with userRoleType=%d for residence residenceId=%d", userRole.getValue(), residenceId));
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Tenancy> query = cb.createQuery(Tenancy.class);
	    Root<Tenancy> root = query.from(Tenancy.class);
	    query = query.select(root)
			.where(cb.equal(root.get("residence"), residenceId));
	   
	    List<Tenancy> usrResidences = entityManager.createQuery(query).getResultList();
	    List<User> usersWithRoleType = usrResidences.stream().map(usrResidence -> usrResidence.getUser())
	    		.filter(user -> user.getRole() == userRole.getValue()).toList();
	    
	    logger.info(String.format("%d users with roleType=%d have been fetched for residence residenceId=%d", userRole.getValue(), usersWithRoleType.size(), residenceId));
	    return usersWithRoleType;
	}
	
	public void save(User user) {
		logger.info(String.format("About to store the user with userId=%d", user.getId()));
		
		entityManager.persist(user);
		
		logger.info(String.format("Successfully stored the user with userId=%d", user.getId()));
	}
	
	public User update(User user) {
		logger.info(String.format("Updating the user with userId=%d", user.getId()));
		
		User updatedUser = entityManager.merge(user);
		
		logger.info(String.format("User with userId=%d has been updated.", user.getId()));
		
		return updatedUser;
	}
	
	public User findByUsername(String username) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> query = cb.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    query = query.select(root)
			.where(cb.equal(root.get("username"), username));
	    
	    try {
	    	return entityManager.createQuery(query).getSingleResult();
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.info(String.format("No users have been found with the username=%s.", username));
			return null;
		}
	}

	public User findByEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> query = cb.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    query = query.select(root)
			.where(cb.equal(root.get("email"), email));
	    
	    try {
	    	return entityManager.createQuery(query).getSingleResult();
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.info(String.format("No users have been found with the email=%s.", email));
			return null;
		}
	}

	public User generateResetPasswordData(User user, String passwordResetToken, Timestamp passwordResetValidUntil) {
		logger.info(String.format("Attempting to set passwordResetToken and passwordResetValidUntil for user with id=%d", user.getId()));
		user.setPasswordResetToken(passwordResetToken);
		user.setPasswordResetValidUntil(passwordResetValidUntil);
		
		user = update(user);

		return user;
	}

	public User resetPasswordForUser(User user, String encrPass) {
		logger.info(String.format("Resetting the password for user with userId=%d", user.getId()));
		user.setPasswordResetToken(null);
		user.setPasswordResetValidUntil(null);
		user.setPassword(encrPass);

		user = update(user);

		return user;
	}
}
