package com.gymbook.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "VERIFICATION_TOKEN")
public class VerificationToken
{
	private static final int EXPIRATION_IN_HOURS = 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "token")
	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiryDate")
	private Date expiryDate;

	public VerificationToken()
	{
		this.expiryDate = calculateExpiryDate(EXPIRATION_IN_HOURS);
		this.token = UUID.randomUUID().toString();
	}

	public VerificationToken(User user)
	{
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION_IN_HOURS);
		this.token = UUID.randomUUID().toString();
	}

	private Date calculateExpiryDate(int expiryTimeInHours)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.HOUR_OF_DAY, expiryTimeInHours);
		return new Date(calendar.getTime().getTime());
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Date getExpiryDate()
	{
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public static int getExpirationInHours()
	{
		return EXPIRATION_IN_HOURS;
	}

	public void updateToken(final String token)
	{
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION_IN_HOURS);
	}

}
