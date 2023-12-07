package com.blog.services;

import com.blog.models.dtos.CommentDTO;

public interface ICommentService {
    void createComment(CommentDTO commentDTO);
}
