package com.gymbook;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.gymbook.dto.in.UserDtoIn;
import com.gymbook.service.UserServiceImpl;

@SpringBootApplication
public class GymBookResourceApplication
{
	
	public static void main(String[] args)
	{
		SpringApplication.run(GymBookResourceApplication.class, args);
	}

	// to share H2 DB on pont 9090
	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server inMemoryH2DatabaseaServer() throws SQLException
	{
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
	}

// // // USER INIT \\ \\ \\
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Bean
	InitializingBean sendDatabase()
	{		
		UserDtoIn userDto = new UserDtoIn("Nagy", "Adam", "nagyadam", "ProbaJelszo136*", "ProbaJelszo136*", "nagy.adam@gmail.com", "36301234567");
		UserDtoIn userDto2 = new UserDtoIn("Kiss", "Bela", "kissbela", "ProbaJelszo136*", "ProbaJelszo136*", "kiss.bela@gmail.com", "36304567891");
		UserDtoIn userDto3 = new UserDtoIn("Szöllősi", "Marcell", "szmarcell", "ProbaJelszo136*", "ProbaJelszo136*", "szllsi.marcell@gmail.com", "36304557891");

		return () -> {
			userServiceImpl.registerNewUserAccount(userDto);
			userServiceImpl.registerNewUserAccount(userDto2);
			userServiceImpl.registerNewUserAccount(userDto3);
		};
	}
	
}
