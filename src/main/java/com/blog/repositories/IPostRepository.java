package com.blog.repositories;

import com.blog.models.Category;
import com.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(long id);

    Optional<Post> findPostBySlug(String slug);

    @Query(value = "select * from posts p order by p.updated_at desc", nativeQuery = true)
    List<Post> findPostsOrderByUpdatedAt();

    @Query(value = "select * from posts p where p.slug != ?1 and p.category_id = ?2 order by p.updated_at desc limit 5", nativeQuery = true)
    List<Post> findRelatedPosts(String slug, long categoryId);

    List<Post> findPostsByPostCategory(Category postCategory);
}
