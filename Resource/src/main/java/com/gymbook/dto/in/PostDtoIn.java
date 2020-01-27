package com.gymbook.dto.in;

import javax.validation.constraints.NotNull;

public class PostDtoIn
{
	@NotNull
	private String title;
	
	@NotNull
	private String content;

	public PostDtoIn()
	{
	}

	public PostDtoIn(String title, String content)
	{
		this.title = title;
		this.content = content;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	public String toString()
	{
		return "PostDtoIn [title=" + title + ", content=" + content + "]";
	}
	
	
	
}
