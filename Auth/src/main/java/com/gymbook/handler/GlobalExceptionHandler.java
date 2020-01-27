package com.gymbook.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gymbook.exception.MissingLoginProcessException;
import com.gymbook.exception.WrongTwoFactorAuthenticationCodeException;
import com.gymbook.validation.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{

	private MessageSource messages;

	@Autowired
	public GlobalExceptionHandler(MessageSource messages)
	{
		this.messages = messages;
	}

// Default Handler : catch-all type of logic that deals with all other exceptions that don’t have specific handlers

	@ExceptionHandler(
	{ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request)
	{
		logger.error("500 Status Code", ex);

		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// BindException : This exception is thrown when fatal binding errors occur. (thrown when the UserDto validated (if invalid).)

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		List<String> errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors())
		{
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors())
		{
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

// HttpMessageNotReadableException : 

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String bodyOfResponse = "This should be application specific";

		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
	}

// MethodArgumentNotValidException : This exception is thrown when request missing parameter

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		List<String> errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors())
		{
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors())
		{
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

// MissingServletRequestPartException : This exception is thrown when when the part of a multipart request not found
// MissingServletRequestParameterException : This exception is thrown when argument annotated with @Valid failed validation

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = ex.getParameterName() + " parameter is missing";

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// ConstrainViolationException : This exception reports the result of constraint violations

	@ExceptionHandler(
	{ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		List<String> errors = new ArrayList<String>();

		for (ConstraintViolation<?> violation : ex.getConstraintViolations())
		{
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// TypeMismatchException : This exception is thrown when try to set bean property with wrong type.
// MethodArgumentTypeMismatchException : This exception is thrown when method argument is not the expected type:

	@ExceptionHandler(
	{ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// NoHandlerFoundException : we can customize our servlet to throw this exception instead of send 404 response

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("404 Status Code", ex);

		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// HttpRequestMethodNotSupportedException : which occurs when you send a requested with an unsupported HTTP method

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("405 Status Code", ex);

		StringBuilder builder = new StringBuilder();

		builder.append(ex.getMethod());

		builder.append(" method is not supported for this request. Supported methods are ");

		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// HttpMediaTypeNotSupportedException : which occurs when the client send a request with unsupported media type

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		logger.error("415 Status Code", ex);

		StringBuilder builder = new StringBuilder();

		builder.append(ex.getContentType());

		builder.append(" media type is not supported. Supported media types are ");

		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

		ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// --------------------
// --------------------
// --------------------
// --------------------
// --------------------

// UserAlreadyExistException : 

	@ExceptionHandler(
	{ MissingLoginProcessException.class })
	public ResponseEntity<Object> handleMissingLoginProcessException(RuntimeException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = messages.getMessage("message.error.missingLoginProcess", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
// WrongTwoFactorAuthenticationCodeException :
	
	@ExceptionHandler(
	{ WrongTwoFactorAuthenticationCodeException.class })
	public ResponseEntity<Object> handleWrongTwoFactorAuthenticationCodeException(RuntimeException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = messages.getMessage("message.error.wrongTfaCode", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}