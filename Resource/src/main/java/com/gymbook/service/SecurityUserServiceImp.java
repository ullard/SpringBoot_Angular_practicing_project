package com.gymbook.service;

import java.util.Arrays;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gymbook.domain.PasswordResetToken;
import com.gymbook.domain.User;
import com.gymbook.exception.InvalidTokenException;
import com.gymbook.repo.PasswordResetTokenRepository;

@Service
@Transactional
public class SecurityUserServiceImp implements SecurityUserService
{

	private PasswordResetTokenRepository passwordTokenRepository;

	@Autowired
	public SecurityUserServiceImp(PasswordResetTokenRepository passwordTokenRepository)
	{
		this.passwordTokenRepository = passwordTokenRepository;
	}

	@Override
	public String validatePasswordResetToken(long id, String token) throws InvalidTokenException
	{
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

		if ((passToken == null) || (passToken.getUser().getId() != id))
		{
			throw new InvalidTokenException("Invalid token: " + token);
		}

		final Calendar cal = Calendar.getInstance();

		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
		{
			throw new InvalidTokenException("Expired token: " + token);
		}
		
		final User user = passToken.getUser();

		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));

		SecurityContextHolder.getContext().setAuthentication(auth);

		return null;
	}

}
