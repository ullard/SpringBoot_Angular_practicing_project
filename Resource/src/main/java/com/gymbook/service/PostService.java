package com.gymbook.service;

import java.util.List;

import com.gymbook.domain.Post;
import com.gymbook.domain.User;
import com.gymbook.dto.in.PostDtoIn;

public interface PostService
{
	public void createPost(User author, User target, PostDtoIn postDto);
	
	public List<Post> findPosts(Long targetId);
}
