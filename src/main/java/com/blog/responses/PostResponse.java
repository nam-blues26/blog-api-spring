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
    private long id;
    private String title;
    private String content;
    private String description;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("category_id")
    private long categoryId ;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    private String image;
    private String slug;
    public static PostResponse fromPost(Post post){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String createdString = post.getCreatedAt().format(formatter);
        String updatedString = post.getUpdatedAt().format(formatter);
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getPostUser().getId())
                .categoryId(post.getPostCategory().getId())
                .createdAt(createdString)
                .updatedAt(updatedString)
                .image(post.getImage())
                .description(post.getDesc())
                .slug(post.getSlug())
                .build();
        return postResponse;
    }
}
