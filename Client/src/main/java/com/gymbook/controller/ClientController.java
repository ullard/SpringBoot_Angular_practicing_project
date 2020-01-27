package com.gymbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController
{
	@RequestMapping(value = "/**/{path:[^.]*}")
	public String index()
	{
		return "forward:/index.html";
	}
}
