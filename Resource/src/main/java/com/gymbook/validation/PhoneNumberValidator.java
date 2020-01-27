package com.gymbook.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String>
{
	private Pattern pattern;
	private Matcher matcher;
	private static final String PHONE_NUMBER_PATTERN = "^[1-9]{1}[0-9]{1,13}$"; //E.164 format

	@Override
	public void initialize(final ValidPhoneNumber validPhoneNumber)
	{

	}

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext context)
	{
		return (validatePhoneNumber(phoneNumber));
	}

	private boolean validatePhoneNumber(String phoneNumber)
	{
		pattern = Pattern.compile(PHONE_NUMBER_PATTERN);

		matcher = pattern.matcher(phoneNumber);

		return matcher.matches();
	}

}
