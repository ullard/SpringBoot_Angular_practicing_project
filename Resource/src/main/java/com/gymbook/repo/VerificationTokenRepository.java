package com.gymbook.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbook.domain.VerificationToken;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long>
{
	@Query(value = "SELECT token FROM VerificationToken token WHERE token.token = :token")
	VerificationToken findByToken(@Param("token") String token);

	@Query(value = "SELECT * FROM verification_token vt WHERE vt.user_id = :id", nativeQuery = true)
	VerificationToken getTokenByUser(@Param("id") Long userId);
}
