package com.blog.controllers;

import com.blog.models.Post;
import com.blog.models.dtos.PostDTO;
import com.blog.responses.PostResponse;
import com.blog.services.IPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            postService.createPost(postDTO);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") long postId) {
        try {
            PostResponse post = postService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") long postId,@Valid @RequestBody  PostDTO postDTO) {
        try {
            postService.updatePost(postId, postDTO);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeletePost(@PathVariable("id") long postId) {
        try {

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
