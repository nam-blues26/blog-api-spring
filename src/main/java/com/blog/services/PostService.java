package com.blog.services;

import com.blog.controllers.PostController;
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

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService{
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IKafkaProducerService kafkaProducerService;

    @Override
    public void createPost(PostDTO postDTO,String image) {
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
                .image(image)
                .desc(postDTO.getDesc())
                .slug(postDTO.getSlug())
                .build();

        Post newPost  = postRepository.saveAndFlush(post);
        String message = "Add Blog success: "+newPost.getId().toString()+ ", created at: "+ newPost.getCreatedAt().toString();
        kafkaProducerService.sendMessage(message);
    }

    @Override
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findPostBySlug(slug).orElseThrow(
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
    public void updatePost(long postId, PostDTO postDTO, String image) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new DataNotFoundException("post is not found")
        );
        Category category = categoryRepository.findCategoryById(postDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setPostCategory(category);
        post.setDesc(postDTO.getDesc());
        post.setImage(image);
        postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {

    }

    @Override
    public List<PostResponse> getPosts() {
        return postRepository.findPostsOrderByUpdatedAt().stream().map(PostResponse::fromPost).toList();
    }

    // Limit 5
    @Override
    public List<PostResponse> getRelatedPost(String slug) {
        Post post = postRepository.findPostBySlug(slug).orElseThrow(
                () -> new DataNotFoundException("post is not found")
        );
        return postRepository.findRelatedPosts(slug, post.getPostCategory().getId()).stream().map(PostResponse::fromPost).toList();
    }
}
