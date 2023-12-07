package com.blog.controllers;

import com.blog.models.Category;
import com.blog.models.dtos.CategoryDTO;
import com.blog.responses.CategoryPostsResponse;
import com.blog.services.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {

        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.categoryList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> GetPostsByCategoryId(@PathVariable("categoryId") long categoryId) {
        try{
        CategoryPostsResponse responses = categoryService.getPostsByCategoryId(categoryId);
            return ResponseEntity.ok(responses);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> DeleteCategory(@PathVariable("categoryId") long categoryId) {
        try{
            categoryService.hideCategory(categoryId);
            return ResponseEntity.ok(true);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
