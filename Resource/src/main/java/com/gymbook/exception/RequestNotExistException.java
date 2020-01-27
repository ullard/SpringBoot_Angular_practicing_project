package com.gymbook.exception;

public class RequestNotExistException extends RuntimeException
{

	private static final long serialVersionUID = -8844735744744201244L;

	public RequestNotExistException()
	{
		super();
	}

	public RequestNotExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RequestNotExistException(String message)
	{
		super(message);
	}

	public RequestNotExistException(Throwable cause)
	{
		super(cause);
	}

}
