package com.gymbook.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gymbook.dto.in.PasswordDto;


public class PasswordMatchChecker implements ConstraintValidator<MatchingPassword, PasswordDto>
{

	@Override
	public void initialize(final MatchingPassword matchingPassword)
	{

	}

	@Override
	public boolean isValid(PasswordDto passwordDto, ConstraintValidatorContext context)
	{
		return (checkPasswordMatch(passwordDto));
	}

	private boolean checkPasswordMatch(PasswordDto passwordDto)
	{
		String p1 = passwordDto.getPassword();

		String p2 = passwordDto.getMatchingPassword();

		if (p1.equals(p2))
		{
			return true;
		}

		return false;
	}

}
