package com.gymbook.exception;

public class WrongTwoFactorAuthenticationCodeException extends RuntimeException
{
	
	private static final long serialVersionUID = -3664238838000809136L;

	public WrongTwoFactorAuthenticationCodeException()
	{
		super();
	}

	public WrongTwoFactorAuthenticationCodeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WrongTwoFactorAuthenticationCodeException(String message)
	{
		super(message);
	}

	public WrongTwoFactorAuthenticationCodeException(Throwable cause)
	{
		super(cause);
	}

}

