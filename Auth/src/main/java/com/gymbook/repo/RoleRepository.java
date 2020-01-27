package com.gymbook.repo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gymbook.model.Role;

@Repository
public class RoleRepository
{
	@Resource(name = "springDSJdbcTemplate")
	private JdbcTemplate jdbc;

	@Autowired
	public RoleRepository(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}

	public List<Role> findByUser(Long id)
	{
		String sql = "SELECT id, name FROM roles WHERE id IN (SELECT role_id FROM users_roles WHERE user_id = ?)";

		return jdbc.query(sql, new Object[]
		{ id }, (rs, i) -> new Role(rs.getLong("id"), rs.getString("name")));
	}
}
