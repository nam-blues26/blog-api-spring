package com.blog.repositories;

import com.blog.models.Category;
import com.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(long id);

    List<Post> findPostsByPostCategory(Category postCategory);
}
