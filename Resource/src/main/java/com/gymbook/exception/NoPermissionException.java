package com.gymbook.exception;

public class NoPermissionException extends RuntimeException
{
	
	private static final long serialVersionUID = 2222039239620947555L;

	public NoPermissionException()
	{
		super();
	}

	public NoPermissionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NoPermissionException(String message)
	{
		super(message);
	}

	public NoPermissionException(Throwable cause)
	{
		super(cause);
	}

}
