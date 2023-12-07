package com.blog.services;

import com.blog.exceptions.DataNotFoundException;
import com.blog.exceptions.ExistData;
import com.blog.models.Category;
import com.blog.models.Post;
import com.blog.models.dtos.CategoryDTO;
import com.blog.repositories.ICategoryRepository;
import com.blog.repositories.IPostRepository;
import com.blog.responses.CategoryPostsResponse;
import com.blog.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService{

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IPostRepository postRepository;
    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())){
            throw new ExistData("Category is exist");
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .active(true)
                .build();
        categoryRepository.save(category);
    }

    @Override
    public List<Category> categoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );
        return category;
    }

    @Override
    public CategoryPostsResponse getPostsByCategoryId(long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );
        List<PostResponse> postResponseList = postRepository.findPostsByPostCategory(category).stream().map(PostResponse::fromPost).toList();
        CategoryPostsResponse response = new CategoryPostsResponse(category.getName(), postResponseList);
        return response;
    }

    @Override
    public void hideCategory(long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId).orElseThrow(
                () -> new DataNotFoundException("Category is not found")
        );
        category.setActive(false);
        categoryRepository.save(category);
    }
}
