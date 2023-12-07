package com.blog.services;

import com.blog.exceptions.DataNotFoundException;
import com.blog.models.Comment;
import com.blog.models.Post;
import com.blog.models.User;
import com.blog.models.dtos.CommentDTO;
import com.blog.repositories.ICommentRepository;
import com.blog.repositories.IPostRepository;
import com.blog.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService implements ICommentService{
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IUserRepository userRepository;
    @Override
    public void createComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserID())
                .orElseThrow(() -> new DataNotFoundException("User is not found"));
        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(
                () -> new DataNotFoundException("post is not found")
        );
        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .commentPost(post)
                .commenUser(user)
                .build();
        commentRepository.save(comment);
    }
}
