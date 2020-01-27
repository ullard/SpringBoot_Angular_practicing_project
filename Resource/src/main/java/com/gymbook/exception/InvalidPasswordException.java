package com.gymbook.exception;

public final class InvalidPasswordException extends RuntimeException
{
	private static final long serialVersionUID = 6784769261490649966L;

	public InvalidPasswordException()
	{
		super();
	}

	public InvalidPasswordException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public InvalidPasswordException(final String message)
	{
		super(message);
	}

	public InvalidPasswordException(final Throwable cause)
	{
		super(cause);
	}

}
