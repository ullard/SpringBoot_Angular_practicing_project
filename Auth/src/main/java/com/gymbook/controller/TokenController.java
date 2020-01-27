package com.gymbook.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * */
@RestController
public class TokenController
{

	@Resource(name = "tokenServices")
	private ConsumerTokenServices tokenServices;

	@Resource(name = "tokenStore")
	private TokenStore tokenStore;

	@PostMapping(path = "/oauth/token/revokeById/{tokenId}")
	public void revokeToken(HttpServletRequest request, @PathVariable String tokenId)
	{
		tokenServices.revokeToken(tokenId);
	}

}