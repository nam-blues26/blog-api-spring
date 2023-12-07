package com.blog.services;

import com.blog.exceptions.DataNotFoundException;
import com.blog.exceptions.ExistData;
import com.blog.models.Category;
import com.blog.models.Post;
import com.blog.models.User;
import com.blog.models.dtos.PostDTO;
import com.blog.repositories.ICategoryRepository;
import com.blog.repositories.IPostRepository;
import com.blog.repositories.IUserRepository;
import com.blog.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService implements IPostService{
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategoryRepository categoryRepository;
    @Override
    public void createPost(PostDTO postDTO) {
        Category category = categoryRepository.findCategoryById(postDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );

        User user = userRepository.findById(postDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("User is not found")
        );
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .postCategory(category)
                .postUser(user)
                .build();

        postRepository.save(post);
    }

    @Override
    public PostResponse getPostById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new DataNotFoundException("post is not found")
        );
        return PostResponse.fromPost(post);
    }

    @Override
    public void updatePost(long postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new DataNotFoundException("post is not found")
        );
        Category category = categoryRepository.findCategoryById(postDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setPostCategory(category);
        postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {

    }
}
