package com.gymbook.dto.in;

import javax.validation.constraints.NotNull;

import com.gymbook.validation.MatchingPassword;
import com.gymbook.validation.ValidPassword;

@MatchingPassword
public class PasswordDto
{

	@ValidPassword
	private String password;

	@NotNull
	private String matchingPassword;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getMatchingPassword()
	{
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword)
	{
		this.matchingPassword = matchingPassword;
	}

}
