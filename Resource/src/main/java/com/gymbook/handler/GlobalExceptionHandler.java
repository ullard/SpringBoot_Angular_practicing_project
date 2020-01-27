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

import com.gymbook.exception.InvalidPasswordException;
import com.gymbook.exception.InvalidRequestException;
import com.gymbook.exception.InvalidTokenException;
import com.gymbook.exception.NoPermissionException;
import com.gymbook.exception.PhoneNumberAlreadyInUseException;
import com.gymbook.exception.RequestAlreadyExistException;
import com.gymbook.exception.RequestNotExistException;
import com.gymbook.exception.UnauthorizedException;
import com.gymbook.exception.UserAlreadyExistException;
import com.gymbook.exception.UserNotFoundException;
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

//	@Override
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request)
//	{
//		logger.error("400 Status Code", ex);
//		
//		String bodyOfResponse = "This should be application specific";
//		
//		// ex.getCause() instanceof JsonMappingException, JsonParseException // for additional information later on
//		
//		return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
//	}

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

// UserAlreadyExistException :  thrown when the user to register with an email that already exists

	@ExceptionHandler(
	{ UserAlreadyExistException.class })
	public ResponseEntity<Object> handleUserAlreadyExist(RuntimeException ex, WebRequest request)
	{
		logger.error("409 Status Code", ex);

		String error = messages.getMessage("message.error.userAlreadyExist", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// PhoneNumberAlreadyInUseException : thrown when the given phone number already in use

	@ExceptionHandler(
	{ PhoneNumberAlreadyInUseException.class })
	public ResponseEntity<Object> handlePhoneNumberAlreadyInUseException(RuntimeException ex, WebRequest request)
	{
		logger.error("412 Status Code", ex);

		String error = messages.getMessage("message.error.phoneNumberAlreadyInUse", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.PRECONDITION_FAILED, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// InvalidTokenException : thrown when registration token doesn't exist or expired

	@ExceptionHandler(
	{ InvalidTokenException.class })
	public ResponseEntity<Object> handleInvalidTokenException(RuntimeException ex, WebRequest request)
	{
		logger.error("404 Status Code", ex);

		String error = messages.getMessage("message.error.invalidToken", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// UserNotFoundException : thrown when a user cannot be found based on the given parameters

	@ExceptionHandler(
	{ UserNotFoundException.class })
	public ResponseEntity<Object> handleUserNotFoundException(RuntimeException ex, WebRequest request)
	{
		logger.error("404 Status Code", ex);

		String error = messages.getMessage("message.error.userNotFound", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
// InvalidPasswordException : 
	
	@ExceptionHandler(
	{ InvalidPasswordException.class })
	public ResponseEntity<Object> handleInvalidPasswordException(RuntimeException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = messages.getMessage("message.error.invalidPassword", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// UnauthorizedException : 

	@ExceptionHandler(
	{ UnauthorizedException.class })
	public ResponseEntity<Object> handleUnauthorizedException(RuntimeException ex, WebRequest request)
	{
		logger.error("401 Status Code", ex);

		String error = messages.getMessage("message.unauthorized", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// RequestAlreadyExistException :

	@ExceptionHandler(
	{ RequestAlreadyExistException.class })
	public ResponseEntity<Object> handleRequestAlreadyExistException(RuntimeException ex, WebRequest request)
	{
		logger.error("409 Status Code", ex);

		String error = messages.getMessage("message.requestAlreadyExist", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// RequestNotExistException :

	@ExceptionHandler(
	{ RequestNotExistException.class })
	public ResponseEntity<Object> handleRequestNotExistException(RuntimeException ex, WebRequest request)
	{
		logger.error("404 Status Code", ex);

		String error = messages.getMessage("message.requestNotExist", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// NoPermissionException : 

	@ExceptionHandler(
	{ NoPermissionException.class })
	public ResponseEntity<Object> handleNoPermissionException(RuntimeException ex, WebRequest request)
	{
		logger.error("403 Status Code", ex);

		String error = messages.getMessage("message.noPermission", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

// InvalidRequestException :

	@ExceptionHandler(
	{ InvalidRequestException.class })
	public ResponseEntity<Object> handleInvalidRequestException(RuntimeException ex, WebRequest request)
	{
		logger.error("400 Status Code", ex);

		String error = messages.getMessage("message.invalidRequest", null, request.getLocale());

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}
