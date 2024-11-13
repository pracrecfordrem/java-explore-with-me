package ru.practicum.service;


import org.springframework.stereotype.Service;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getCategories(Long from, Long size) {
        return categoryRepository.getCategories(from, size);
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.getCategoryById(catId);
    }

    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }
}
