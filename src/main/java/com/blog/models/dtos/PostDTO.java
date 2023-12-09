package com.blog.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    private String title;
    private String content;

    private String desc;

    @JsonProperty("category_id")
    private Long categoryId;

    private String slug;
    @JsonProperty("user_id")
    private Long userId;

}
