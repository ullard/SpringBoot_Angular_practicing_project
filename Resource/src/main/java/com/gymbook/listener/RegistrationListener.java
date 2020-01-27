package com.gymbook.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.gymbook.domain.User;
import com.gymbook.domain.VerificationToken;
import com.gymbook.event.OnRegistrationCompleteEvent;
import com.gymbook.repo.VerificationTokenRepository;
import com.gymbook.service.UserServiceImpl;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{

	private UserServiceImpl service;

	private MessageSource messages;

	private JavaMailSender mailSender;
	
	private VerificationTokenRepository tokenRepository;
	
	@Autowired
	public RegistrationListener(UserServiceImpl service, MessageSource messages, JavaMailSender mailSender, VerificationTokenRepository tokenRepository)
	{
		this.service = service;
		this.messages = messages;
		this.mailSender = mailSender;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event)
	{
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event)
	{
		User user = event.getUser();

		service.createVerificationToken(user);
		
		VerificationToken token = tokenRepository.getTokenByUser(user.getId());

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token.getToken();
		
		String message = messages.getMessage("message.regSucc", null, event.getLocale());

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + "\n\n" + confirmationUrl);
		mailSender.send(email);
	}
}
