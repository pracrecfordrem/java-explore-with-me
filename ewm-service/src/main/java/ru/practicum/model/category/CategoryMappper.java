package ru.practicum.model.category;

public class CategoryMappper {
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(null,newCategoryDto.getName());
    }
}
