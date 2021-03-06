package com.gymbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable
{
	private static final long serialVersionUID = 2076184140944500474L;

	private Long id;

	private String username;

	private String password;

	private List<Role> roles = new ArrayList<Role>();

	private boolean isUsing2FA;
	
	private String secret;

	public User()
	{
	}

	public User(Long id, String username, String password, boolean isUsing2fa, String secret)
	{
		this.id = id;
		this.username = username;
		this.password = password;
		this.isUsing2FA = isUsing2fa;
		this.secret = secret;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public List<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(List<Role> roles)
	{
		this.roles = roles;
	}

//

	public void addRole(Role role)
	{
		this.roles.add(role);
	}

//

	public boolean isUsing2FA()
	{
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA)
	{
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret()
	{
		return secret;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}

}
