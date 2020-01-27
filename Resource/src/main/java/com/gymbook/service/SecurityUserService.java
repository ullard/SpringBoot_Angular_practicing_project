package com.gymbook.service;

public interface SecurityUserService
{
	public String validatePasswordResetToken(long id, String token);
}
