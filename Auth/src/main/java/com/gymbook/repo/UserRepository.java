package com.gymbook.repo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gymbook.model.User;

@Repository
public class UserRepository
{
	@Resource(name = "springDSJdbcTemplate")
	private JdbcTemplate jdbc;

	@Autowired
	public UserRepository(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}

	public User findByUsername(String username)
	{

		String sql = "SELECT id, username, password, is_using2fa, secret FROM users WHERE username = ?";

		return jdbc.queryForObject(sql, new Object[]
		{ username }, (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getBoolean("is_using2fa"), rs.getString("secret")));

	}

}
