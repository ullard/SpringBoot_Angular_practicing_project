package com.gymbook.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbook.domain.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long>
{
	@Query(value = "SELECT * FROM posts WHERE target_id = :targetId ORDER BY upload_date DESC", nativeQuery = true)
	List<Post> findPosts(@Param("targetId") Long targetId);

}
