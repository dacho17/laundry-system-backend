package com.laundrysystem.backendapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.UserRole;

public class UserDetailsHelper implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsHelper.class);
	
	private int id;
	private String username;
	private String password;
	private short role;
	
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsHelper(int id, String username, String password, short role,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.authorities = authorities;
	}
	
	public static UserDetailsHelper build(User user) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.getRole(user.getRole()).name()));
		
		UserDetailsHelper usrDet = new UserDetailsHelper(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getRole(),
				authorities);
		
		logger.info(String.format("UserDetailsHelper object has successfully been built. [UserDetailsHelper=%s].", usrDet));
		return usrDet;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public short getRole() {
		return role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
