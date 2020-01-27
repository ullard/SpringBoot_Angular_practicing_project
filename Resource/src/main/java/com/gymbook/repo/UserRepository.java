package com.gymbook.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbook.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
	
	@Query(value = "SELECT * FROM users u WHERE u.username = :username", nativeQuery = true)
	User findByUsername(@Param("username") String username);
	
	@Query(value = "SELECT * FROM users u WHERE u.id = :id", nativeQuery = true)
	User findUserById(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
	User findByEmail(@Param("email") String email);

	@Query(value = "SELECT * FROM users u WHERE LOWER(name) LIKE CONCAT('%',LOWER(:string),'%') OR LOWER(username) LIKE CONCAT('%',LOWER(:string),'%')", nativeQuery = true)
	List<User> findUsers(@Param("string") String string);

	@Query(value = "SELECT * FROM users u WHERE u.phone = :phone", nativeQuery = true)
	User findByPhoneNumber(@Param("phone") String string);
	
}
