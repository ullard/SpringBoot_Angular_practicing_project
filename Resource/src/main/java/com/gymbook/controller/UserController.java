package com.gymbook.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.gymbook.domain.User;
import com.gymbook.domain.VerificationToken;
import com.gymbook.dto.in.PasswordDto;
import com.gymbook.dto.in.UserDtoIn;
import com.gymbook.dto.out.UserDtoOut;
import com.gymbook.event.OnRegistrationCompleteEvent;
import com.gymbook.exception.InvalidPasswordException;
import com.gymbook.service.SecurityUserServiceImp;
import com.gymbook.service.UserServiceImpl;
import com.gymbook.validation.GenericResponse;

@RestController
@RequestMapping("/user")
public class UserController
{
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private UserServiceImpl userServiceImpl;

	private ApplicationEventPublisher eventPublisher;

	private MessageSource messages;

	private JavaMailSender mailSender;

	private Environment env;

	private SecurityUserServiceImp securityUserServiceImp;

	@Autowired
	public UserController(UserServiceImpl userServiceImpl, ApplicationEventPublisher eventPublisher, MessageSource messages, JavaMailSender mailSender, Environment env, SecurityUserServiceImp securityUserServiceImp)
	{
		this.userServiceImpl = userServiceImpl;
		this.eventPublisher = eventPublisher;
		this.messages = messages;
		this.mailSender = mailSender;
		this.env = env;
		this.securityUserServiceImp = securityUserServiceImp;
	}

	@GetMapping(path = "/current")
	public String getCurrentUser()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return "Currently loggen in user: " + auth;
	}
	
	@GetMapping(path = "/search/{searchValue}")
	public List<UserDtoOut> getUsers(@PathVariable String searchValue)
	{
		List<User> users = userServiceImpl.findUsers(searchValue);
		
		List<UserDtoOut> usersOut = new ArrayList<>();
		
		for (User user : users)
		{
			UserDtoOut userOut = new UserDtoOut(user.getUsername(), user.getName(), user.getEmail(), user.getPhone());
			
			usersOut.add(userOut);
		}
		
		return usersOut;
	}

// GET USER : 

	@GetMapping(path = "/username/{username}")
	public UserDtoOut getUser(@PathVariable String username)
	{
		User user = userServiceImpl.findByUsername(username);

		UserDtoOut userDto = new UserDtoOut(user.getUsername(), user.getName(), user.getEmail(), user.getPhone());

		return userDto;
	}

// REGISTRATION : receives data from a frontend form via HttpServletRequest and using it to register the user 

	@PostMapping(path = "/registration")
	public GenericResponse registerUserAccount(@Valid UserDtoIn accountDto, HttpServletRequest request)
	{
		LOGGER.debug("Registering user account with information: {}", accountDto);

		User registered = userServiceImpl.registerNewUserAccount(accountDto);
		
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		
		return new GenericResponse(messages.getMessage("message.regSucc", null, request.getLocale()));

	}

	private String getAppUrl(HttpServletRequest request)
	{
		return env.getProperty("frontend.registrationConfirm");
	}

// REGISTRATION CONFIRM WITH TOKEN

	@PostMapping(path = "/registrationConfirm")
	public GenericResponse confirmRegistration(WebRequest request, @RequestParam("token") String token)
	{
		VerificationToken verificationToken = userServiceImpl.getVerificationToken(token);

		User user = verificationToken.getUser();

		userServiceImpl.saveRegisteredUser(user);

		return new GenericResponse(messages.getMessage("message.regConfSucc", null, request.getLocale()));
	}

// PASSWORD CHANGES SECTION (3)

	// RESET PASSWORD --> after this CHANGE PASSWORD section below

	@PostMapping(path = "/resetPassword")
	public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail)
	{
		final User user = userServiceImpl.findByEmail(userEmail);
		
		if (user != null)
		{
			final String token = UUID.randomUUID().toString();
			
			userServiceImpl.createPasswordResetTokenForUser(user, token);
			
			mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		}

		return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}

	private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user)
	{
		final String url = contextPath + "/passwordChange?id=" + user.getId() + "&token=" + token;

		final String message = messages.getMessage("message.resetPassword", null, locale);
		
		final String subject = messages.getMessage("message.resetPasswordEmailSubject", null, locale);

		return constructEmail(subject, message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user)
	{
		final SimpleMailMessage email = new SimpleMailMessage();

		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		email.setFrom(env.getProperty("support.email"));

		return email;
	}

	// CHANGE PASSWORD

	@PostMapping(path = "/changePassword")
	public GenericResponse changePassword(final Locale locale, @RequestParam("id") final long id, @RequestParam("token") final String token, @Valid PasswordDto passwordDto) throws Exception
	{
		final String result = securityUserServiceImp.validatePasswordResetToken(id, token);

		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		userServiceImpl.changeUserPassword(user, passwordDto.getPassword());

		return new GenericResponse(messages.getMessage("message.resetPasswordSucc", null, locale));
	}

// RESEND VERIFICATION EMAIL

	@GetMapping(path = "/resendRegistrationToken")
	public GenericResponse resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken)
	{
		VerificationToken newToken = userServiceImpl.generateNewVerificationToken(existingToken);

		User user = userServiceImpl.getUserByToken(newToken.getToken());

		String appUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);

		mailSender.send(email);

		return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user)
	{
		final String confirmationUrl = contextPath + "/registrationConfirm?token=" + newToken.getToken();

		final String message = messages.getMessage("message.resendToken", null, locale);

		return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
	}

// MODIFY USER PASSWORD (after register and log in)

	@PostMapping(path = "/modifyPassword")
	public GenericResponse changeUserPassword(Locale locale, @RequestParam("newPassword") String password, @RequestParam("oldPassword") String oldPassword)
	{
		User user = userServiceImpl.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userServiceImpl.checkIfValidOldPassword(user, oldPassword))
		{
			throw new InvalidPasswordException("The entered password does not match the current password.");
		}

		userServiceImpl.changeUserPassword(user, password);

		return new GenericResponse(messages.getMessage("message.modifyPasswordSucc", null, locale));
	}

// TOTP QR

	@GetMapping(path = "/tfa")
	public String getTfa()
	{
		User user = userServiceImpl.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		if (user.isUsing2FA())
		{
			String prefix = env.getProperty("url.qrPrefix");

			try
			{
				return prefix + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", "DiplomaProjekt", user.getEmail(), user.getSecret(), "PTE"), "UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		return "";
	}

	@PostMapping(path = "/tfa")
	public GenericResponse changeTfa(Locale locale, @RequestParam("usingTfa") final boolean usingTfa)
	{
		User user = userServiceImpl.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		userServiceImpl.chageTfa(user, usingTfa);
		
		return new GenericResponse(messages.getMessage("message.changeTfaSucc", null, locale));
	}

}