package com.blog.services;

import com.blog.models.Post;
import com.blog.models.dtos.PostDTO;
import com.blog.responses.PostResponse;

import java.util.Optional;

public interface IPostService {
    void createPost(PostDTO postDTO);

    PostResponse getPostById(long postId);

    void updatePost(long postId, PostDTO postDTO);

    void deletePost(long postId);
}
