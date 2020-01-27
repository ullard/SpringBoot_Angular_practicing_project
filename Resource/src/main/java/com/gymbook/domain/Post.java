package com.gymbook.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "post")
@Table(name = "POSTS")
public class Post
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private User author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_id")
	private User target;
	
	@Column(name = "title")
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "upload_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	@Column(name = "last_change", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChangeDate;

	public Post()
	{
	}

	public Post(User author, User target, String title, String content)
	{
		this.author = author;
		this.target = target;
		this.title = title;
		this.content = content;
		this.uploadDate = new Date();
		this.lastChangeDate = new Date();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getAuthor()
	{
		return author;
	}

	public void setAuthor(User author)
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
