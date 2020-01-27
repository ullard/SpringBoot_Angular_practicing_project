package com.gymbook.dto.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gymbook.validation.MatchingUserPassword;
import com.gymbook.validation.ValidEmail;
import com.gymbook.validation.ValidPassword;
import com.gymbook.validation.ValidPhoneNumber;
import com.gymbook.validation.ValidUsername;

@MatchingUserPassword
public class UserDtoIn
{

	@NotNull
	@Size(min = 1, message = "{Size.userDto.firstName}")
	private String firstname;

	@NotNull
	@Size(min = 1, message = "{Size.userDto.lastName}")
	private String lastname;

	@ValidUsername
	@NotNull
	@Size(min = 3, max = 50, message = "{Size.userDto.username}")
	private String username;

	@ValidPassword
	@NotNull
	private String password;

	@NotNull
	private String matchingPassword;

	@ValidEmail
	@NotNull
	@Size(min = 3, message = "{Size.userDto.email}")
	private String email;

	@ValidPhoneNumber
	@NotNull
	private String phone;

	public UserDtoIn()
	{
	}

	public UserDtoIn(@NotNull @Size(min = 1) String firstname, @NotNull @Size(min = 1) String lastname, @NotNull @Size(min = 3) String username, @NotNull @NotEmpty String password, @NotNull String matchingPassword, @NotNull @Size(min = 3) String email, /*@NotNull @NotEmpty UserType userType,*/ @NotNull @NotEmpty String phone)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.matchingPassword = matchingPassword;
		this.email = email;
		this.phone = phone;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	@Override
	public String toString()
	{
		return "UserDto [firstname=" + firstname + ", lastname=" + lastname + ", username=" + username + ", password=" + password + ", matchingPassword=" + matchingPassword + ", email=" + email + /*", userType=" + userType +*/ ", phone=" + phone + "]";
	}

}
