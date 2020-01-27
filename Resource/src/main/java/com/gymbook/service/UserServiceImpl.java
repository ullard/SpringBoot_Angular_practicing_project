package com.gymbook.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gymbook.domain.PasswordResetToken;
import com.gymbook.domain.Role;
import com.gymbook.domain.User;
import com.gymbook.domain.VerificationToken;
import com.gymbook.dto.in.UserDtoIn;
import com.gymbook.exception.InvalidTokenException;
import com.gymbook.exception.PhoneNumberAlreadyInUseException;
import com.gymbook.exception.UserAlreadyExistException;
import com.gymbook.exception.UserNotFoundException;
import com.gymbook.repo.PasswordResetTokenRepository;
import com.gymbook.repo.RoleRepository;
import com.gymbook.repo.UserRepository;
import com.gymbook.repo.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService
{
	private final String USER_ROLE = "ROLE_USER";

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private VerificationTokenRepository tokenRepository;

	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, VerificationTokenRepository tokenRepository, PasswordResetTokenRepository passwordResetTokenRepository)
	{
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.tokenRepository = tokenRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}

// SAVE USER TO DATABASE - - - nem lehet benne a éles kódban

	@Override
	public void save(User user)
	{
		try
		{
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setRoles(new HashSet<>(roleRepository.findAll())); // ???????????????????
			userRepository.save(user);
		} catch (Exception e)
		{
			System.out.println("Hiba a user mentésekor: " + e);
		}
	}

// FIND USER BY USERNAME

	/**
	 * Searches for a user with the given username. (only returns the full match)
	 */
	@Override
	public User findByUsername(String username) throws UserNotFoundException
	{
		User user = userRepository.findByUsername(username);

		if (user == null)
		{
			throw new UserNotFoundException("There is no user with the given username: " + username);
		}

		return user;
	}

// FIND USER BY ID

	/**
	 * Searches for a user by id.
	 */
	@Override
	public User findById(Long id) throws UserNotFoundException
	{
		User user = userRepository.findUserById(id);

		if (user == null)
		{
			throw new UserNotFoundException("There is no user with the given id: " + id);
		}

		return user;
	}

// FIND USER BY EMAIL

	/**
	 * Searches for a user by email.
	 */
	@Override
	public User findByEmail(String email) throws UserNotFoundException
	{
		User user = userRepository.findByEmail(email);

		if (user == null)
		{
			throw new UserNotFoundException("There is no user with the given email: " + email);
		}

		return user;
	}

// FIND USER BY GIVEN STRING (name or email)

	/**
	 * Searches for users by string. (returns those that contain the given string)
	 */
	@Override
	public List<User> findUsers(String string)
	{
		return userRepository.findUsers(string);
	}

// USER REGISTRATION

	/**
	 * Register a new user with the given parameters.
	 */
	@Transactional
	@Override
	public User registerNewUserAccount(UserDtoIn accountDto) throws UserAlreadyExistException, PhoneNumberAlreadyInUseException
	{
		if (emailExists(accountDto.getEmail()))
		{
			throw new UserAlreadyExistException("There is already an account with that email adress: " + accountDto.getEmail());
		}

		if (usernameExists(accountDto.getUsername()))
		{
			throw new UserAlreadyExistException("There is already an account with that username: " + accountDto.getUsername());
		}

		if (phoneNumberAlreadyExists(accountDto.getPhone()))
		{
			throw new PhoneNumberAlreadyInUseException("This phone number is already in use: +" + accountDto.getPhone());
		}
		
		User user = new User();

		ArrayList<String> roles = new ArrayList<>();

		roles.add(USER_ROLE);

		user.setName(accountDto.getFirstname() + " " + accountDto.getLastname());
		user.setUsername(accountDto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
		user.setEmail(accountDto.getEmail());
		user.setPhone("+" + accountDto.getPhone());

		// nem lehet éles kódban
		user.setEnabled(true);
//		user.setUsing2FA(true);
		// nem lehet éles kódban

		for (int i = 0; i < roles.size(); i++)
		{
			Role role = roleRepository.findByName(roles.get(i));

			if (role != null)
			{
				user.addRole(role);
			} else
			{
				user.addRole(new Role(roles.get(i)));
			}
		}

		return userRepository.save(user);
	}

	private boolean emailExists(String email)
	{
		User user = userRepository.findByEmail(email);

		if (user != null)
		{
			return true;
		}

		return false;
	}

	private boolean usernameExists(String username)
	{
		User user = userRepository.findByUsername(username);

		if (user != null)
		{
			return true;
		}

		return false;
	}

	private boolean phoneNumberAlreadyExists(String phoneNumber)
	{
		User user = userRepository.findByPhoneNumber("+" + phoneNumber);

		if (user != null)
		{
			return true;
		}

		return false;
	}

// CREATE VERIFICATION TOKEN

	/**
	 * */
	@Transactional
	@Override
	public void createVerificationToken(User user)
	{
		VerificationToken token = new VerificationToken(user);
		tokenRepository.save(token);
	}

// GET VERIFICATION TOKEN

	/**
	 * */
	@Override
	public VerificationToken getVerificationToken(String token) throws InvalidTokenException
	{
		VerificationToken verificationToken = tokenRepository.findByToken(token);

		Calendar cal = Calendar.getInstance();

		if (verificationToken == null)
		{
			throw new InvalidTokenException("There is no such verification token: " + token);
		}

		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
		{
			throw new InvalidTokenException("Verification token expired");
		}

		return verificationToken;
	}

// SAVE USER AFTER TOKEN CONFIRMATION

	/**
	 * */
	@Transactional
	@Override
	public void saveRegisteredUser(User user)
	{
		user.setEnabled(true);

		userRepository.save(user);
	}

// CREATE PASSWORD RESET TOKEN

	/**
	 * */
	@Transactional
	@Override
	public void createPasswordResetTokenForUser(final User user, final String token)
	{
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

// CHANGE USER PASSWORD

	/**
	 * */
	@Transactional
	@Override
	public void changeUserPassword(final User user, final String password)
	{
		user.setPassword(bCryptPasswordEncoder.encode(password));

		userRepository.save(user);
	}

// NEW VERIFICATION TOKEN (part of "resend verification email")

	/**
	 * */
	@Transactional
	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken)
	{
		VerificationToken token = tokenRepository.findByToken(existingVerificationToken);

		token.updateToken(UUID.randomUUID().toString());

		token = tokenRepository.save(token);

		return token;
	}

	/**
	 * */
	@Override
	public User getUserByToken(String verificationToken)
	{
		User user = tokenRepository.findByToken(verificationToken).getUser();

		return user;
	}

// MODIFY PASSWORD (after register and log in)

	/**
	 * */
	@Transactional
	@Override
	public boolean checkIfValidOldPassword(final User user, final String oldPassword)
	{
		return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
	}

// MODIFY TFA
	
	@Override
	public void chageTfa(User user, boolean usingTfa)
	{
		if (!usingTfa)
		{
			user.setSecret(Base32.random());
		}
		
		user.setUsing2FA(usingTfa);
		
		userRepository.save(user);
	}

}
