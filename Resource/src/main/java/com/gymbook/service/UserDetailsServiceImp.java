package com.gymbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbook.domain.User;
import com.gymbook.repo.UserRepository;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImp implements UserDetailsService
{

	private UserRepository userRepository;

//	private LoginAttemptService loginAttemptService;

//	private HttpServletRequest request;

	@Autowired
	public UserDetailsServiceImp(UserRepository userRepository, LoginAttemptService loginAttemptService)
//			HttpServletRequest request)
	{
		this.userRepository = userRepository;
//		this.loginAttemptService = loginAttemptService;
//		this.request = request;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
//		String ip = getClientIP();

//		if (loginAttemptService.isBlocked(ip))
//		{
//			throw new RuntimeException("blocked");
//		}

		User user = userRepository.findByUsername(username);

		if (user == null)
		{
			throw new UsernameNotFoundException(username);
		}

		return new UserDetailsImpl(user);
	}

//	private final String getClientIP()
//	{
//		final String xfHeader = request.getHeader("X-Forwarded-For");
//
//		if (xfHeader == null)
//		{
//			return request.getRemoteAddr();
//		}
//
//		return xfHeader.split(",")[0];
//	}

}