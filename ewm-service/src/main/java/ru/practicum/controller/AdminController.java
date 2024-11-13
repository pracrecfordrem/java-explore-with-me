package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.user.NewUserDto;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;
import ru.practicum.service.CategoryService;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMappper;
import ru.practicum.model.category.NewCategoryDto;
import ru.practicum.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(CategoryMappper.toCategory(newCategoryDto)), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody NewUserDto newUserDto) {
        return new ResponseEntity<>(userService.createUser(UserMapper.toUser(newUserDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Category> updateCategory(@RequestBody NewCategoryDto newCategoryDto, @PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        if (category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            category.setName(newCategoryDto.getName());
            return new ResponseEntity<>(categoryService.createCategory(category),HttpStatus.OK);
        }
    }

}
