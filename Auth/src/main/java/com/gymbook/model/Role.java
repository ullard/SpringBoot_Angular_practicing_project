package com.gymbook.model;

import java.io.Serializable;

public class Role implements Serializable
{
	private static final long serialVersionUID = -7587891914836500161L;

	private Long id;

	private String name;

	public Role()
	{
	}

	public Role(Long id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public Role(String name)
	{
		this.name = name;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "Role [id=" + id + ", name=" + name + "]";
	}

}
