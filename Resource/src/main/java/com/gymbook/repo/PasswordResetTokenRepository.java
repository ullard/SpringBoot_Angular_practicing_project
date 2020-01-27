package com.gymbook.repo;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gymbook.domain.PasswordResetToken;
import com.gymbook.domain.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>
{

	public PasswordResetToken findByToken(String token);

	public PasswordResetToken findByUser(User user);

	public Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

	public void deleteByExpiryDateLessThan(Date now);

	@Modifying
	@Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
	public void deleteAllExpiredSince(Date now);
}
