package com.blog.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPostsResponse {
    private String categoryName;

    @JsonProperty("postList")
    private List<PostResponse> postResponseList;
}
