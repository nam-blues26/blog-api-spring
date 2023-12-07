package com.blog.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String title;
    private String content;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("user_id")
    private Long userId;

}
