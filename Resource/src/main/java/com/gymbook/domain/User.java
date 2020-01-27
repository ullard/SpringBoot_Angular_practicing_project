package com.gymbook.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.aerogear.security.otp.api.Base32;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "USERS")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "username", nullable = false, length = 50, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "phone", unique = true)
	private String phone;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private VerificationToken token; // !!!

	@Column(name = "enabled")
	private Boolean enabled;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval = true)
	@JsonBackReference
	private List<Post> writtenPosts;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "target", orphanRemoval = true)
	@JsonBackReference
	private List<Post> recievedPosts;

	@Temporal(TemporalType.DATE)
	@Column(name = "reg_date")
	private Date registrationDate;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns =
	{ @JoinColumn(name = "user_id") }, inverseJoinColumns =
	{ @JoinColumn(name = "role_id") })
	@JsonIgnoreProperties("users")
	private Set<Role> roles;

	private boolean isUsing2FA;

	private String secret;

	public User()
	{
		this.registrationDate = new Date();
		this.enabled = false;
		this.isUsing2FA = false;
		this.secret = Base32.random();
	}

	public User(String username, String password, String name, String email, String phone)
	{
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.registrationDate = new Date();
		this.enabled = false;
		this.isUsing2FA = false;
		this.secret = Base32.random();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public VerificationToken getToken()
	{
		return token;
	}

	public void setToken(VerificationToken token)
	{
		this.token = token;
	}

	public Boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}

	public List<Post> getWrittenPosts()
	{
		return writtenPosts;
	}

	public void setWrittenPosts(List<Post> writtenPosts)
	{
		this.writtenPosts = writtenPosts;
	}

	public List<Post> getRecievedPosts()
	{
		return recievedPosts;
	}

	public void setRecievedPosts(List<Post> recievedPosts)
	{
		this.recievedPosts = recievedPosts;
	}

	public Date getRegistrationDate()
	{
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

//

	public void addRole(Role role)
	{
		if (this.roles == null || this.roles.isEmpty())
		{
			this.roles = new HashSet<Role>();
		}

		this.roles.add(role);
	}

//	2FA

	public boolean isUsing2FA()
	{
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA)
	{
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret()
	{
		return secret;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}

//

	@Override
	public String toString()
	{
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", phone=" + phone + ", token=" + token + ", enabled=" + enabled + ", writtenPosts=" + writtenPosts + ", registrationDate=" + registrationDate + ", roles=" + roles + "]";
	}

}
