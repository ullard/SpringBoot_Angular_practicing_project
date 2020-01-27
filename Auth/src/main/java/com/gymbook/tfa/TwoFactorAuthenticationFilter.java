package com.gymbook.tfa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gymbook.model.User;
import com.gymbook.service.UserServiceImpl;

/**
 *
 */
public class TwoFactorAuthenticationFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(TwoFactorAuthenticationFilter.class);

	public static final String ROLE_TWO_FACTOR_AUTHENTICATED = "ROLE_TWO_FACTOR_AUTHENTICATED";
	
	private CustomOAuth2RequestFactory oAuth2RequestFactory;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	public void setClientDetailsService(ClientDetailsService clientDetailsService)
	{
		oAuth2RequestFactory = new CustomOAuth2RequestFactory(clientDetailsService);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{

		if ((request.getSession().getAttribute("user") == null))
		{
			if ((request.getParameter("username") != null) && (request.getParameter("password") != null))
			{
				request.getSession().setAttribute("username", request.getParameter("username"));
				request.getSession().setAttribute("password", request.getParameter("password"));
				request.getSession().setAttribute("grant_type", request.getParameter("grant_type"));
				request.getSession().setAttribute("client_id", request.getParameter("client_id"));
			} else
			{
				return;
			}

			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(request.getSession().getAttribute("username").toString(), request.getSession().getAttribute("password").toString());
			Authentication auth = authenticationManager.authenticate(authReq);

			User user = userService.findByUsername(request.getSession().getAttribute("username").toString());

			if (user == null)
			{
				throw new UsernameNotFoundException(request.getSession().getAttribute("username").toString());
			}

			request.getSession().setAttribute("user", user);
		}

		if (((User) request.getSession().getAttribute("user")).isUsing2FA())
		{

			if (!hasRole(ROLE_TWO_FACTOR_AUTHENTICATED, ((User) request.getSession().getAttribute("user"))))
			{

				LOG.debug("doFilterInternal(): redirecting to {}", TwoFactorAuthenticationController.PATH);

				System.out.println(request.getSession().getId());

				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/secure/two_factor_authentication");
				dispatcher.forward(request, response);

				return;
			}

		}

		LOG.debug("doFilterInternal(): without redirect.");
		
		paramsFromRequest(request);
		
		Map<String, String[]> additionalParams = new HashMap<String, String[]>();
		additionalParams.put("username", new String[] {request.getSession().getAttribute("username").toString()});
		additionalParams.put("password", new String[] {request.getSession().getAttribute("password").toString()});
		additionalParams.put("grant_type", new String[] {request.getSession().getAttribute("grant_type").toString()});
		additionalParams.put("client_id", new String[] {request.getSession().getAttribute("client_id").toString()});
		EnhancedHttpRequest enhancedHttpRequest = new EnhancedHttpRequest((HttpServletRequest) request, additionalParams);
		
		request.getSession().invalidate();
		
		filterChain.doFilter(enhancedHttpRequest, response);
	}

	private boolean hasRole(String checkedRole, User user)
	{
		return user.getRoles().stream().anyMatch(role -> checkedRole.equals(role.getName()));
	}

	private Map<String, String> paramsFromRequest(HttpServletRequest request)
	{
		Map<String, String> params = new HashMap<>();

		for (Entry<String, String[]> entry : request.getParameterMap().entrySet())
		{
			System.out.println(entry);
			
			params.put(entry.getKey(), entry.getValue()[0]);
		}

		return params;
	}

}
