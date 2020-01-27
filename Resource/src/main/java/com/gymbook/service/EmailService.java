package com.gymbook.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class EmailService
{
	private final Log log = LogFactory.getLog(this.getClass());

	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailService(JavaMailSender javaMailSender)
	{
		this.javaMailSender = javaMailSender;
	}

	@Async
	public void sendEmail(String email)
	{
		SimpleMailMessage message = null;

		try
		{
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Sikeres regisztrálás"); // i18n !!!
			message.setText("Kedves " + email + "! \n \n Köszönjük, hogy regisztráltál az oldalunkra!"); //i18n !!!

			javaMailSender.send(message);
		} catch (Exception e)
		{
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + "  " + e); //i18n !!!
		}
	}
}
