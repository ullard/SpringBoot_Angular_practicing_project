package com.gymbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gymbook.model.User;
import com.gymbook.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	/**
	 * Searches for a user with the given username. (only returns the full match)
	 */
	@Override
	public User findByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findByUsername(username);

		if (user == null)
		{
			throw new UsernameNotFoundException("There is no user with the given username: " + username);
		}

		return user;
	}
}
