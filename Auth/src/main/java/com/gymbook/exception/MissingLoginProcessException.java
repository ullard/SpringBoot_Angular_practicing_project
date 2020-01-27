package com.gymbook.exception;

public class MissingLoginProcessException extends RuntimeException
{
	
	private static final long serialVersionUID = 2611801244846090955L;

	public MissingLoginProcessException()
	{
		super();
	}

	public MissingLoginProcessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MissingLoginProcessException(String message)
	{
		super(message);
	}

	public MissingLoginProcessException(Throwable cause)
	{
		super(cause);
	}

}
