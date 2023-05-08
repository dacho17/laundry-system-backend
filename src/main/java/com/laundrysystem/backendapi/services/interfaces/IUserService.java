package com.laundrysystem.backendapi.services.interfaces;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.laundrysystem.backendapi.dtos.AuthRequest;
import com.laundrysystem.backendapi.dtos.UserDto;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;

public interface IUserService {
//	UserDto createUser(AuthRequest signupRequest) throws DbException;
	UserDto loginUser(AuthRequest loginRequest) throws DbException;
	UserDto logoutUser() throws DbException;
	
	User fetchUserByUsername(String username) throws UsernameNotFoundException, DbException;
	UserDto removeJwtFromUser(String jwt, String username) throws UsernameNotFoundException, DbException;
}
