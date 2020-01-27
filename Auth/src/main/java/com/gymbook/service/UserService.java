package com.gymbook.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gymbook.model.User;

public interface UserService
{
	public User findByUsername(String username) throws UsernameNotFoundException;
}
