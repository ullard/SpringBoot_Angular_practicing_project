package com.gymbook.exception;

public class InvalidRequestException extends RuntimeException
{

	private static final long serialVersionUID = 7525418820539278370L;

	public InvalidRequestException()
	{
		super();
	}

	public InvalidRequestException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidRequestException(String message)
	{
		super(message);
	}

	public InvalidRequestException(Throwable cause)
	{
		super(cause);
	}

}
