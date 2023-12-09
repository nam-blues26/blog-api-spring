package com.blog.services;

import com.blog.controllers.PostController;
import com.blog.models.Post;
import com.blog.models.dtos.PostDTO;
import com.blog.responses.PostResponse;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    void createPost(PostDTO postDTO, String image);

    PostResponse getPostBySlug(String slug);

    void updatePost(long postId, PostDTO postDTO);

    void deletePost(long postId);

    List<PostResponse> getPosts();
}
