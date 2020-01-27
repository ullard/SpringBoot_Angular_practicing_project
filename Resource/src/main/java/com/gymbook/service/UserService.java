package com.gymbook.service;

import java.util.List;

import com.gymbook.domain.User;
import com.gymbook.domain.VerificationToken;
import com.gymbook.dto.in.UserDtoIn;
import com.gymbook.exception.InvalidTokenException;
import com.gymbook.exception.PhoneNumberAlreadyInUseException;
import com.gymbook.exception.UserAlreadyExistException;
import com.gymbook.exception.UserNotFoundException;

public interface UserService
{
	// Éles kódban nem lehet benne!!!
	public void save(User user);
	// Éles kódban nem lehet benne!!!

	public List<User> findUsers(String string);

	public User findByEmail(String email) throws UserNotFoundException;
	
	public User findById(Long id) throws UserNotFoundException;

	public User findByUsername(String username) throws UserNotFoundException;

	public User registerNewUserAccount(UserDtoIn accountDto) throws UserAlreadyExistException, PhoneNumberAlreadyInUseException; //, UserTypeNotExistException;

	public void createVerificationToken(User user);

	public VerificationToken getVerificationToken(String token) throws InvalidTokenException;

	public void saveRegisteredUser(User user);

	public void createPasswordResetTokenForUser(User user, String token);

	public void changeUserPassword(User user, String password);

	public VerificationToken generateNewVerificationToken(String token);

	public User getUserByToken(String verificationToken);

	public boolean checkIfValidOldPassword(User user, String oldPassword);
	
	public void chageTfa(User user, boolean usingTfa);

}
