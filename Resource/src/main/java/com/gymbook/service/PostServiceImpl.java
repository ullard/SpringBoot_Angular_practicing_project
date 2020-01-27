package com.gymbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbook.domain.Post;
import com.gymbook.domain.User;
import com.gymbook.dto.in.PostDtoIn;
import com.gymbook.repo.PostRepository;

@Service
public class PostServiceImpl implements PostService
{
	private PostRepository postRepository;

	@Autowired
	public PostServiceImpl(PostRepository postRepository)
	{
		this.postRepository = postRepository;
	}

	@Override
	public void createPost(User author, User target, PostDtoIn postDto)
	{
		 Post post = new Post(author, target, postDto.getTitle(), postDto.getContent());
		 
		 postRepository.save(post);
	}

	@Override
	public List<Post> findPosts(Long targetId)
	{
		return postRepository.findPosts(targetId);
	}

}
