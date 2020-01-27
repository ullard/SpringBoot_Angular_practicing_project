package com.gymbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbook.model.Role;
import com.gymbook.repo.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService
{

	private RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository)
	{
		this.roleRepository = roleRepository;
	}
	
	@Override
	public List<Role> findByUser(Long userId)
	{
		return roleRepository.findByUser(userId);
	}

}
