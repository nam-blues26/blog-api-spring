package com.blog.repositories;

import com.blog.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);

    Optional<Category> findCategoryById(long id);
}
