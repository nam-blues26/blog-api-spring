package com.blog.services;

import com.blog.responses.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IPostRedisService {
    List<PostResponse> getPostsRedis() throws JsonProcessingException;
    void savePostsRedis(List<PostResponse> posts) throws JsonProcessingException;
}
