package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.Category;
import ru.practicum.service.CategoryService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class PublicController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories(@RequestParam(defaultValue = "0") Long from, @RequestParam(defaultValue = "10") Long size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Category> getCategories(@PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
    }
}
