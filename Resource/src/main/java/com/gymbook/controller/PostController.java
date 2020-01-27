package com.gymbook.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.gymbook.domain.Post;
import com.gymbook.domain.User;
import com.gymbook.dto.in.PostDtoIn;
import com.gymbook.dto.out.PostDtoOut;
import com.gymbook.dto.out.UserDtoOut;
import com.gymbook.exception.UnauthorizedException;
import com.gymbook.service.PostServiceImpl;
import com.gymbook.service.UserService;
import com.gymbook.validation.GenericResponse;

@RestController
@RequestMapping("/post")
public class PostController
{
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private PostServiceImpl postServiceImpl;

	private UserService userService;
	
	private MessageSource messages;

	@Autowired
	public PostController(PostServiceImpl postServiceImpl, UserService userService, MessageSource messages)
	{
		this.postServiceImpl = postServiceImpl;
		this.userService = userService;
		this.messages = messages;
	}

	@Secured("ROLE_USER")
	@PostMapping(path = "/createPost")
	public GenericResponse createPost(Locale locale, WebRequest request, @Valid PostDtoIn postDto, String targetUsername) throws UnauthorizedException
	{
		LOGGER.debug("Creating a post with information: {}", postDto);

		User author = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		User target = userService.findByUsername(targetUsername);

		postServiceImpl.createPost(author, target, postDto);
		
		return new GenericResponse(messages.getMessage("message.createPostSucc", null, locale));
	}

	@GetMapping(path = "/posts/{username}")
	public List<PostDtoOut> getPosts(@PathVariable String username)
	{
		
		List<Post> posts = postServiceImpl.findPosts(userService.findByUsername(username).getId());
		
		List<PostDtoOut> postsDto = new ArrayList<>();
		
		for (Post post : posts)
		{
			UserDtoOut userDto = new UserDtoOut(post.getAuthor().getUsername(), post.getAuthor().getName(), post.getAuthor().getEmail(), post.getAuthor().getPhone());
			
			PostDtoOut postDto = new PostDtoOut(userDto, post.getTitle(), post.getContent(), post.getUploadDate(), post.getLastChangeDate());
			
			postsDto.add(postDto);
		}
		
		return postsDto;
	}
}
