package com.gymbook.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String>
{
	private Pattern pattern;
	private Matcher matcher;
	private static final String USERNAME_PATTERN = "[A-Za-z0-9]+{3,50}";

	@Override
	public void initialize(final ValidUsername validUsername)
	{

	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context)
	{
		return (validateUsername(username));
	}
	
	private boolean validateUsername(String username)
	{
		pattern = Pattern.compile(USERNAME_PATTERN);
		
		matcher = pattern.matcher(username);
		
		return matcher.matches();
	}

}
