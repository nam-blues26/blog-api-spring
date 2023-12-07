package com.blog.responses;

import com.blog.models.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String title;
    private String content;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("category_id")
    private long categoryId ;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    public static PostResponse fromPost(Post post){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String createdString = post.getCreatedAt().format(formatter);
        String updatedString = post.getUpdatedAt().format(formatter);
        PostResponse postResponse = PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getPostUser().getId())
                .categoryId(post.getPostCategory().getId())
                .createdAt(createdString)
                .updatedAt(updatedString)
                .build();
        return postResponse;
    }
}
