package com.gymbook.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gymbook.model.Role;
import com.gymbook.model.User;
import com.gymbook.repo.RoleRepository;
import com.gymbook.repo.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findByUsername(username);

		if (user == null)
		{
			throw new UsernameNotFoundException(username);
		}

		List<Role> roles = roleRepository.findByUser(user.getId());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(roles));
	}

	private static List<GrantedAuthority> getAuthorities(List<Role> roles)
	{
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles)
		{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return authorities;
	}

}
