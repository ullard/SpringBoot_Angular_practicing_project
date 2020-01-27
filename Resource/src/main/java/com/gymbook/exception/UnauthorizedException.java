package com.gymbook.exception;

public class UnauthorizedException extends RuntimeException
{

	private static final long serialVersionUID = 60982149842552844L;

	public UnauthorizedException()
	{
		super();
	}

	public UnauthorizedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public UnauthorizedException(String message)
	{
		super(message);
	}

	public UnauthorizedException(Throwable cause)
	{
		super(cause);
	}

}
