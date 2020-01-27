package com.gymbook.tfa;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 */
public class CustomOAuth2RequestFactory extends DefaultOAuth2RequestFactory
{

	private static final Logger LOG = LoggerFactory.getLogger(CustomOAuth2RequestFactory.class);

	public static final String SAVED_AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE_NAME = "savedAuthorizationRequest";

	public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService)
	{
		super(clientDetailsService);
	}

	@Override
	public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters)
	{

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

		HttpSession session = attr.getRequest().getSession(false);

		if (session != null)
		{
			AuthorizationRequest authorizationRequest = (AuthorizationRequest) session.getAttribute(SAVED_AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE_NAME);

			if (authorizationRequest != null)
			{
				session.removeAttribute(SAVED_AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE_NAME);

				LOG.debug("createAuthorizationRequest(): return saved copy.");

				return authorizationRequest;
			}
		}

		LOG.debug("createAuthorizationRequest(): create");

		return super.createAuthorizationRequest(authorizationParameters);
	}

}
