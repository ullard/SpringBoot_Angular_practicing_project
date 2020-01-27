package com.gymbook.dto.out;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PostDtoOut
{
	private UserDtoOut author;

	private String title;

	private String content;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date uploadDate;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastChangeDate;

	public PostDtoOut()
	{
	}

	public PostDtoOut(UserDtoOut author, String title, String content, Date uploadDate, Date lastChangeDate)
	{
		super();
		this.author = author;
		this.title = title;
		this.content = content;
		this.uploadDate = uploadDate;
		this.lastChangeDate = lastChangeDate;
	}

	public UserDtoOut getAuthor()
	{
		return author;
	}

	public void setAuthor(UserDtoOut author)
	{
		this.author = author;
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

	public Date getUploadDate()
	{
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate)
	{
		this.uploadDate = uploadDate;
	}

	public Date getLastChangeDate()
	{
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate)
	{
		this.lastChangeDate = lastChangeDate;
	}

}
