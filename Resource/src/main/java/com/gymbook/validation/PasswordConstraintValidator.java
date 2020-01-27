package com.gymbook.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import com.google.common.base.Joiner;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String>
{

	@Override
	public void initialize(final ValidPassword arg0)
	{

	}

	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context)
	{
		// @formatter:off
		List<Rule> rules = new ArrayList<>();
		rules.add(new LengthRule(8, 30)); // Rule 1: Password length should be in between 8 and 16 characters
		rules.add(new WhitespaceRule()); // Rule 2: No whitespace allowed
		rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1)); // Rule 3.a: At least one Upper-case character
		rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));// Rule 3.b: At least one Lower-case character
		rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));// Rule 3.c: At least one digit
		rules.add(new CharacterRule(EnglishCharacterData.Special, 1));// Rule 3.d: At least one special character
		rules.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, false)); // QWERTY SEQUENCE
		rules.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false)); // ALPHABETICAL SEQUENCE
		rules.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false)); // NUMERICAL SEQUENCE

		final PasswordValidator validator = new PasswordValidator(rules);

		final RuleResult result = validator.validate(new PasswordData(password));

		if (result.isValid())
		{
			return true;
		}

		context.disableDefaultConstraintViolation();

		context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)))
				.addConstraintViolation();

		return false;
	}

}
