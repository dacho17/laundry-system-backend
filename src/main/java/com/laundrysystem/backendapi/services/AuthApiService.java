package com.laundrysystem.backendapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.utils.UserDetailsHelper;

@Service
public class AuthApiService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(AuthApiService.class);
	
	@Autowired
	private IUserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DbException {
		User user = fetchUserByUsername(username);

		logger.info(String.format("Successfully retrieved and about to build UserDetails object for the user with [username=%s].", username));
		return UserDetailsHelper.build(user);
	}
	
	@Transactional
	public void removeJwtFromUser(String jwt, String username) throws UsernameNotFoundException, DbException {
		User user = fetchUserByUsername(username);
		
		try {
			userRepository.setUserJwt(user.getId(), null);
		} catch (Exception exc) {
			logger.warn(String.format("Error occurred while removing jwt from the user with [userId=%d].", user.getId()));
			throw new DbException();
		}
	}

	private User fetchUserByUsername(String username) throws UsernameNotFoundException, DbException {
		logger.info(String.format("Attempting to find the user with [username=%s].", username));
		User user;
		
		try {
			user = userRepository.findByUsername(username);			
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while fetching the user by username=%s. - err=[%s]", username, exc.getStackTrace().toString()));
			throw new DbException();
		}
		
		if (user == null) {
			logger.warn(String.format("User not found with [username=%s].", username));
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}
		
		return user;
	}
}
