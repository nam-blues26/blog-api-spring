package com.blog.services;

import com.blog.models.Category;
import com.blog.models.Post;
import com.blog.models.dtos.CategoryDTO;
import com.blog.responses.CategoryPostsResponse;

import java.util.List;

public interface ICategoryService {
    void createCategory(CategoryDTO categoryDTO);
    List<Category> categoryList ();
    Category getCategoryById(long categoryId);

    CategoryPostsResponse getPostsByCategoryId(long categoryId);
    void hideCategory(long categoryId);
}
