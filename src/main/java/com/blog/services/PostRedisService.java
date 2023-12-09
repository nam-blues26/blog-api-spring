package com.blog.services;

import com.blog.responses.PostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostRedisService implements IPostRedisService{
    public static final String KEY_POST_REDIS = "all-posts";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper redisObjectMapper;
    @Override
    public List<PostResponse> getPostsRedis() throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get(KEY_POST_REDIS);
        List<PostResponse> postResponseList = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<PostResponse>>() {}) : new ArrayList<>();
        return postResponseList;
    }


    @Override
    public void savePostsRedis(List<PostResponse> posts) throws JsonProcessingException {
        String json = redisObjectMapper.writeValueAsString(posts);
        redisTemplate.opsForValue().set(KEY_POST_REDIS,json);
    }
}
