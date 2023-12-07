package com.blog.models.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String content;
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("user_id")
    private Long userID;
}
