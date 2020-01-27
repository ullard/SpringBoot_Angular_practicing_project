package com.gymbook.exception;

public class InvalidTokenException extends RuntimeException
{
	
	private static final long serialVersionUID = -460332257532314297L;
	
	public InvalidTokenException()
	{
		super();
	}

	public InvalidTokenException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidTokenException(String message)
	{
		super(message);
	}

	public InvalidTokenException(Throwable cause)
	{
		super(cause);
	}

}
