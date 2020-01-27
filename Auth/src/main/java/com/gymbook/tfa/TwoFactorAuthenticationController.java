package com.gymbook.tfa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymbook.exception.MissingLoginProcessException;
import com.gymbook.exception.WrongTwoFactorAuthenticationCodeException;
import com.gymbook.model.Role;
import com.gymbook.model.User;
import com.gymbook.validation.GenericResponse;

@RestController
@RequestMapping(TwoFactorAuthenticationController.PATH)
public class TwoFactorAuthenticationController
{

//	private static final Logger LOG = LoggerFactory.getLogger(TwoFactorAuthenticationController.class);

	public static final String PATH = "/secure/two_factor_authentication";

	@PostMapping(path = "")
	public GenericResponse authGet(HttpServletRequest request, HttpServletResponse response) throws MissingLoginProcessException
	{

		if (request.getSession().getAttribute("user") == null)
		{
			throw new MissingLoginProcessException("Missing login process.");
		}

		return new GenericResponse("OK");
	}

	@PostMapping(path = "/verification")
	public GenericResponse authPost(HttpServletRequest request, HttpServletResponse response, @NotNull String verificationCode)
	{
		
		if (request.getSession().getAttribute("user") != null && request.getParameter("verificationCode") != null)
		{

			if (correct2FASecret(request, request.getParameter("verificationCode")))
			{
				User user = ((User) request.getSession().getAttribute("user"));

				Role role = new Role(TwoFactorAuthenticationFilter.ROLE_TWO_FACTOR_AUTHENTICATED);

				user.addRole(role);

				request.getSession().setAttribute("user", user);

				return new GenericResponse("OK");

			} else
			{
				throw new WrongTwoFactorAuthenticationCodeException("Wrong TwoFactor Authentication code.");
			}

		} else
		{
			throw new MissingLoginProcessException("Missing login process.");
		}

	}

	private boolean correct2FASecret(HttpServletRequest request, String verificationCode)
	{
		Totp totp = new Totp(((User) request.getSession().getAttribute("user")).getSecret());

		if (totp.verify(verificationCode))
		{
			return true;
		}

		return false;
	}

}
