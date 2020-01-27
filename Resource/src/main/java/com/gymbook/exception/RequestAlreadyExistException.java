package com.gymbook.exception;

public class RequestAlreadyExistException extends RuntimeException
{

	private static final long serialVersionUID = -8366386785167264502L;

	public RequestAlreadyExistException()
	{
		super();
	}

	public RequestAlreadyExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RequestAlreadyExistException(String message)
	{
		super(message);
	}

	public RequestAlreadyExistException(Throwable cause)
	{
		super(cause);
	}

}