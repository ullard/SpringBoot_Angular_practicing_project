package com.gymbook.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gymbook.dto.in.UserDtoIn;

public class UserPasswordMatchChecker implements ConstraintValidator<MatchingUserPassword, UserDtoIn>
{

	@Override
	public void initialize(final MatchingUserPassword matchingPassword)
	{

	}

	@Override
	public boolean isValid(UserDtoIn userDto, ConstraintValidatorContext context)
	{
		return (checkPasswordMatch(userDto));
	}
	
	private boolean checkPasswordMatch(UserDtoIn userDto)
	{
		String p1 = userDto.getPassword();
		
		String p2 = userDto.getMatchingPassword();
		
		if (p1.equals(p2))
		{
			return true;
		}
		
		return false;
	}

}
