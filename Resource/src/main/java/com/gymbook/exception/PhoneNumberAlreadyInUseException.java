package com.gymbook.exception;

public class PhoneNumberAlreadyInUseException extends RuntimeException
{
	private static final long serialVersionUID = 8747816787032833889L;

	public PhoneNumberAlreadyInUseException()
	{
		super();
	}

	public PhoneNumberAlreadyInUseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PhoneNumberAlreadyInUseException(String message)
	{
		super(message);
	}

	public PhoneNumberAlreadyInUseException(Throwable cause)
	{
		super(cause);
	}

}
