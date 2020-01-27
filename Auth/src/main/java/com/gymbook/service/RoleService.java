package com.gymbook.service;

import java.util.List;

import com.gymbook.model.Role;

public interface RoleService
{
	public List<Role> findByUser(Long userId); 
}
